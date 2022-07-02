package com.plantfam.plantfam.ui.screens.addplantscreen

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.util.Log
import androidx.annotation.MainThread
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.neverEqualPolicy
import androidx.compose.runtime.setValue
import androidx.documentfile.provider.DocumentFile
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.amplifyframework.core.Amplify
import com.amplifyframework.storage.StorageAccessLevel
import com.amplifyframework.storage.options.StorageUploadInputStreamOptions
import com.plantfam.plantfam.TAG
import com.plantfam.plantfam.plantFamApp
import com.plantfam.plantfam.network.model.Plant
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ViewModelScoped
import id.zelory.compressor.Compressor
import id.zelory.compressor.constraint.format
import id.zelory.compressor.constraint.quality
import io.realm.OrderedCollectionChangeSet
import io.realm.OrderedRealmCollectionChangeListener
import io.realm.Realm
import io.realm.RealmResults
import io.realm.kotlin.where
import io.realm.mongodb.User
import io.realm.mongodb.sync.SyncConfiguration
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.bson.types.ObjectId
import java.io.File
import java.io.InputStream
import java.nio.file.Files
import java.nio.file.Paths
import javax.inject.Inject

@HiltViewModel
class AddPlantViewModel @Inject constructor(
    @SuppressLint("StaticFieldLeak") @ApplicationContext private val application: Context, // No memory leaks here: https://stackoverflow.com/a/66217924/13026376
) : ViewModel() {
    private lateinit var realm: Realm
    var user: User? = null

    var plants: List<Plant> by mutableStateOf(listOf(), neverEqualPolicy())
        private set

    private lateinit var plantsList: RealmResults<Plant>

    init {
        user = plantFamApp.currentUser()
        instantiateSyncedRealm()
    }

    private fun instantiateSyncedRealm() {
        val config = SyncConfiguration.defaultConfig(user!!, user!!.id)

        // Sync all realm changes via a new instance
        Realm.getInstanceAsync(config, object : Realm.Callback() {
            override fun onSuccess(realm: Realm) {
                // since this realm should live exactly as long as this Composable/ViewModel, assign the realm to a member variable
                this@AddPlantViewModel.realm = realm
                getPlants()
            }
        })
    }

    private fun getPlants() {
        plantsList = realm.where<Plant>().findAllAsync()

        val plantsChangeListener =
            OrderedRealmCollectionChangeListener { updatedResult: RealmResults<Plant>, _: OrderedCollectionChangeSet ->
                Log.i(TAG(), "Fetched new plants data.")
                plants = updatedResult.freeze()
            }

        plantsList.addChangeListener(plantsChangeListener)
    }

    /**
     * Compresses cover photo image, then adds the plant to Realm (minus the coverPhoto field).
     */
    fun addPlant(plant: Plant) {
        if(!plant.coverPhoto.isNullOrBlank()) {
            val coverPhotoUri = Uri.parse(plant.coverPhoto)
            val coverPhotoPath: String? = coverPhotoUri.path
            val coverPhotoFileName = DocumentFile.fromSingleUri(
                application,
                Uri.parse(plant.coverPhoto)
            )?.name
            val coverPhotoFile = FileUtil.from(application, coverPhotoUri)

            if (coverPhotoPath != null && coverPhotoFileName != null) {
                viewModelScope.launch {
                    val compressedCoverPhotoFile = compressImage(coverPhotoFile)

                    updatePlantCoverPhoto(
                        plant.id,
                        coverPhotoFileName,
                        compressedCoverPhotoFile.inputStream()
                    )
                }
            }
        }

        realm.executeTransactionAsync {
            if(plant.parent != null) {
                val plantRealmObject = it.where(Plant::class.java)
                    .equalTo("id", plant.parent!!.id)
                    .findFirst()
                plant.parent = plantRealmObject
            }

            Log.i(TAG(), "Adding plant.")
            it.insert(plant)
        }
    }

    private suspend fun compressImage(imageFile: File): File {
        val compressedImage = Compressor.compress(
            application,
            imageFile,
            Dispatchers.Main // TODO: #7 this upload doesn't work if not run on the main thread
        ) {
            quality(80)
            format(Bitmap.CompressFormat.WEBP)
        }

        Log.i(TAG(), "compressed image")
        return compressedImage
    }

    /**
     * Uploads the image to S3, then finds the plant in the realm and updates its coverphoto field accordingly.
     */
    private fun updatePlantCoverPhoto(
        plantId: ObjectId,
        imageName: String,
        imageInputStream: InputStream?
    ) {
        val uploadKey = "${user!!.id}/${imageName}"
        Log.d(TAG(), "upload key: $uploadKey")

        val options = StorageUploadInputStreamOptions.builder()
            .accessLevel(StorageAccessLevel.PRIVATE)
            .build()

        Amplify.Storage.uploadInputStream(uploadKey, imageInputStream!!, options,
            { storageUploadInputStreamResult ->
                Log.i(TAG(), "Cover photo uploaded: ${storageUploadInputStreamResult.key}")

                realm.executeTransactionAsync {
                    val plant = it.where(Plant::class.java)
                        .equalTo("id", plantId)
                        .findFirst()
                    plant!!.coverPhoto = storageUploadInputStreamResult.key
                    Log.i(TAG(), "Plant coverPhoto updated.")
                }
            },
            { Log.e(TAG(), "Cover photo upload failed: ", it.cause) }
        )
    }

    override fun onCleared() {
        super.onCleared()
        Log.i(TAG(), "Clearing ${TAG()}.")
        realm.close()
    }
}