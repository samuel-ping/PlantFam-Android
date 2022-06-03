package com.plantfam.plantfam.manageplantsscreen

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.neverEqualPolicy
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.amplifyframework.core.Amplify
import com.amplifyframework.storage.StorageAccessLevel
import com.amplifyframework.storage.StorageException
import com.amplifyframework.storage.options.StorageDownloadFileOptions
import com.amplifyframework.storage.options.StorageGetUrlOptions
import com.plantfam.plantfam.TAG
import com.plantfam.plantfam.plantFamApp
import com.plantfam.plantfam.service.model.Plant
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import hilt_aggregated_deps._dagger_hilt_android_internal_modules_ApplicationContextModule
import io.realm.*
import io.realm.kotlin.where
import io.realm.mongodb.User
import io.realm.mongodb.sync.SyncConfiguration
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.net.URL
import javax.inject.Inject

@HiltViewModel
class ManagePlantsViewModel @Inject constructor(
        @SuppressLint("StaticFieldLeak") @ApplicationContext private val application: Context, // No memory leaks here: https://stackoverflow.com/a/66217924/13026376
) : ViewModel() {
    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing: StateFlow<Boolean>
        get() = _isRefreshing.asStateFlow()

    private lateinit var realm: Realm
    private var user: User? = plantFamApp.currentUser()

    var plants: List<Plant> by mutableStateOf(listOf(), neverEqualPolicy())
        private set

    private lateinit var plantsList: RealmResults<Plant>

    init {
        instantiateSyncedRealm()
    }

    private fun instantiateSyncedRealm() {
        val config = SyncConfiguration.defaultConfig(user!!, user!!.id)

        Log.d(TAG(), "Config: partitionValue=${user!!.id}")

        // Sync all realm changes via a new instance, and when that instance has been successfully created connect it to an on-screen list (a recycler view)
        Realm.getInstanceAsync(config, object : Realm.Callback() {
            override fun onSuccess(realm: Realm) {
                // since this realm should live exactly as long as this activity, assign the realm to a member variable
                this@ManagePlantsViewModel.realm = realm
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

                for (plant in plants) {
                    plant.coverPhoto?.let { getPhoto(it) }
                }
            }

        plantsList.addChangeListener(plantsChangeListener)
    }

    /**
     * If the photo does not exist on the local device, then download it.
     */
    private fun getPhoto(s3Key: String) {
        val localImage = File("${application.filesDir}/$s3Key")

        if(localImage.exists()) {
            Log.v(TAG(), "Image $s3Key exists on local device.")
            return
        }

        Log.v(TAG(), "Image $s3Key doesn't exist on local device. Downloading photo.")

        val options = StorageDownloadFileOptions.builder()
            .accessLevel(StorageAccessLevel.PRIVATE)
            .build()

        Amplify.Storage.downloadFile(
            s3Key,
            localImage,
            options,
            { Log.v(TAG(), "Image successfully downloaded.") },
            { Log.e(TAG(), "Failed download", it) }
        )
    }

    fun refresh() {
        Log.i(TAG(), "Refreshing plants.")
        viewModelScope.launch {
            _isRefreshing.emit(true)
            getPlants()
            _isRefreshing.emit(false)
        }
    }

    override fun onCleared() {
        super.onCleared()
        Log.i(TAG(), "Clearing ${TAG()}.")
        plantsList.removeAllChangeListeners()
        realm.close()
    }
}