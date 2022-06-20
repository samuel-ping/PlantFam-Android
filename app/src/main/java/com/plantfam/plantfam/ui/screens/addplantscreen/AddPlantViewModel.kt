package com.plantfam.plantfam.ui.screens.addplantscreen

import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.amplifyframework.core.Amplify
import com.amplifyframework.storage.StorageAccessLevel
import com.amplifyframework.storage.options.StorageUploadInputStreamOptions
import com.plantfam.plantfam.TAG
import com.plantfam.plantfam.plantFamApp
import com.plantfam.plantfam.network.model.Plant
import com.plantfam.plantfam.network.repository.ShrimpifyRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import io.realm.Realm
import io.realm.mongodb.User
import io.realm.mongodb.sync.SyncConfiguration
import kotlinx.coroutines.launch
import org.bson.types.ObjectId
import java.io.InputStream
import javax.inject.Inject

@HiltViewModel
class AddPlantViewModel @Inject constructor(
    @SuppressLint("StaticFieldLeak") @ApplicationContext private val application: Context, // No memory leaks here: https://stackoverflow.com/a/66217924/13026376
) : ViewModel() {
    private lateinit var realm: Realm
    var user: User? = null

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
            }
        })
    }

    fun addPlant(plant: Plant) {
        // upload coverPhoto to S3, then store url in plant
        Log.d(TAG(), "coverphoto uri: " + plant.coverPhoto)
        val coverPhotoUri = Uri.parse(plant.coverPhoto)

        Log.d(TAG(), "calling shrimpify repo compressimage() now")
//        viewModelScope.launch {
            ShrimpifyRepository().compressImage(
                ::updatePlantCoverPhoto,
                plant.id,
                coverPhotoUri,
                application
            )
//        }

        // uploadKey in the form of userId/imageUrl
//        val uploadKey = "${user!!.id}/${
//            DocumentFile.fromSingleUri(
//                application,
//                Uri.parse(plant.coverPhoto)
//            )?.name
//        }"

//        val coverPhotoInputStream: InputStream? =
//            application.contentResolver.openInputStream(Uri.parse(plant.coverPhoto))
//
//        val options = StorageUploadInputStreamOptions.builder()
//            .accessLevel(StorageAccessLevel.PRIVATE)
//            .build()
//
//        Amplify.Storage.uploadInputStream(uploadKey, coverPhotoInputStream!!, options,
//            { storageUploadInputStreamResult ->
//                Log.i(TAG(), "Cover photo uploaded: ${storageUploadInputStreamResult.key}")
//
//                plant.coverPhoto = storageUploadInputStreamResult.key
//
                realm.executeTransactionAsync {
                    Log.i(TAG(), "Adding plant.")
                    it.insert(plant)
                }
//            },
//            { Log.e(TAG(), "Cover photo upload failed: ", it.cause) }
//        )
    }

    /**
     * Uplaods the image to S3, then finds the plant in the realm and updates its coverphoto field accordingly.
     */
    private fun updatePlantCoverPhoto(
        plantId: ObjectId,
        imageName: String,
        imageInputStream: InputStream?
    ) {
        // uploadKey in the form of userId/imageUrl
        val uploadKey = "${user!!.id}/${imageName}"

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