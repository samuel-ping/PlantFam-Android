package com.plantfam.plantfam.ui.screens.plantdetailsscreen

import android.util.Log
import androidx.lifecycle.ViewModel
import com.plantfam.plantfam.TAG
import com.plantfam.plantfam.plantFamApp
import com.plantfam.plantfam.network.model.Plant
import dagger.hilt.android.lifecycle.HiltViewModel
import io.realm.Realm
import io.realm.kotlin.where
import io.realm.mongodb.User
import io.realm.mongodb.sync.SyncConfiguration
import org.bson.types.ObjectId
import javax.inject.Inject

@HiltViewModel
class PlantDetailsViewModel @Inject constructor() : ViewModel() {
    private lateinit var realm: Realm
    private var user: User? = plantFamApp.currentUser()

    init {
        instantiateSyncedRealm()
    }

    private fun instantiateSyncedRealm() {
        val config =
            SyncConfiguration.Builder(user!!, user!!.id).allowQueriesOnUiThread(true).build()

        // Sync all realm changes via a new instance, and when that instance has been successfully created connect it to an on-screen list (a recycler view)
        realm = Realm.getInstance(config)
    }

    fun getPlant(plantId: String): Plant? =
        realm.where<Plant>().equalTo("_id", ObjectId(plantId)).findFirst()

    override fun onCleared() {
        super.onCleared()
        Log.i(TAG(), "Clearing PlantDetailsViewModel.")
        realm.close()
    }
}