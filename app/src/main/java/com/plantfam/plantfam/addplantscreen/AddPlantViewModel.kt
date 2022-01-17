package com.plantfam.plantfam.addplantscreen

import android.util.Log
import androidx.lifecycle.ViewModel
import com.plantfam.plantfam.TAG
import com.plantfam.plantfam.plantFamApp
import com.plantfam.plantfam.service.model.Plant
import dagger.hilt.android.lifecycle.HiltViewModel
import io.realm.Realm
import io.realm.mongodb.User
import io.realm.mongodb.sync.SyncConfiguration
import javax.inject.Inject

@HiltViewModel
class AddPlantViewModel @Inject constructor() : ViewModel() {
    private lateinit var realm: Realm
    var user: User? = null

    init {
        user = plantFamApp.currentUser()
        instantiateSyncedRealm()
    }

    private fun instantiateSyncedRealm() {
        val config = SyncConfiguration.defaultConfig(user!!, "${user!!.id}")

        // Sync all realm changes via a new instance, and when that instance has been successfully created connect it to an on-screen list (a recycler view)
        Realm.getInstanceAsync(config, object : Realm.Callback() {
            override fun onSuccess(realm: Realm) {
                // since this realm should live exactly as long as this activity, assign the realm to a member variable
                this@AddPlantViewModel.realm = realm
            }
        })
    }

    fun addPlant(plant: Plant) {
        realm.executeTransactionAsync {
            Log.i(TAG(), "Adding plant.")
            it.insert(plant)
        }
    }

    override fun onCleared() {
        super.onCleared()
        Log.i(TAG(), "Clearing ${TAG()}.")
        realm.close()
    }
}