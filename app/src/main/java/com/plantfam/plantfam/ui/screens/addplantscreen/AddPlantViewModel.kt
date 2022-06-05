package com.plantfam.plantfam.ui.screens.addplantscreen

import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.documentfile.provider.DocumentFile
import androidx.lifecycle.ViewModel
import com.amplifyframework.core.Amplify
import com.amplifyframework.storage.StorageAccessLevel
import com.amplifyframework.storage.options.StorageUploadInputStreamOptions
import com.plantfam.plantfam.TAG
import com.plantfam.plantfam.plantFamApp
import com.plantfam.plantfam.network.model.Plant
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import io.realm.Realm
import io.realm.mongodb.User
import io.realm.mongodb.sync.SyncConfiguration
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

        // Sync all realm changes via a new instance, and when that instance has been successfully created connect it to an on-screen list (a recycler view)
        Realm.getInstanceAsync(config, object : Realm.Callback() {
            override fun onSuccess(realm: Realm) {
                // since this realm should live exactly as long as this activity, assign the realm to a member variable
                this@AddPlantViewModel.realm = realm
            }
        })
    }

    fun addPlant(plant: Plant) {
        // upload coverPhoto to S3, then store url in plant
        Log.d(TAG(), "coverphoto uri: " + plant.coverPhoto)

        val coverPhotoPath: String? = Uri.parse(plant.coverPhoto).path

        Log.d(TAG(), "FILEPATH: $coverPhotoPath")

        // uploadKey in the form of userId/imageUrl
        val uploadKey = "${user!!.id}/${
            DocumentFile.fromSingleUri(
                application,
                Uri.parse(plant.coverPhoto)
            )?.name
        }"

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
//                realm.executeTransactionAsync {
//                    Log.i(TAG(), "Adding plant.")
//                    it.insert(plant)
//                }
//            },
//            { Log.e(TAG(), "Cover photo upload failed: ", it.cause) }
//        )
    }

    override fun onCleared() {
        super.onCleared()
        Log.i(TAG(), "Clearing ${TAG()}.")
        realm.close()
    }
}