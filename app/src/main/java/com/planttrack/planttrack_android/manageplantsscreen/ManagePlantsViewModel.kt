package com.planttrack.planttrack_android.manageplantsscreen

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.neverEqualPolicy
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.planttrack.planttrack_android.TAG
import com.planttrack.planttrack_android.plantTrackApp
import com.planttrack.planttrack_android.service.model.Plant
import dagger.hilt.android.lifecycle.HiltViewModel
import io.realm.*
import io.realm.kotlin.where
import io.realm.mongodb.User
import io.realm.mongodb.sync.SyncConfiguration
import javax.inject.Inject

@HiltViewModel
class ManagePlantsViewModel @Inject constructor() : ViewModel() {
    private var user: User? = plantTrackApp.currentUser()
    private var userRealm: Realm? = null


    var plants: List<Plant> by mutableStateOf(listOf(), neverEqualPolicy())
        private set

    private lateinit var plantsList: RealmResults<Plant>

    init {
        instantiateSyncedRealm()
    }

    private fun instantiateSyncedRealm() {
//        val config = SyncConfiguration.Builder(user = user!!,  partitionValue = "user=${user!!.id}").build()
        val config = SyncConfiguration.defaultConfig(user!!, "user=${user!!.id}")
//        syncedRealm = Realm.getInstance(config)
        // Sync all realm changes via a new instance, and when that instance has been successfully created connect it to an on-screen list (a recycler view)
        Realm.getInstanceAsync(config, object : Realm.Callback() {
            override fun onSuccess(realm: Realm) {
                // since this realm should live exactly as long as this activity, assign the realm to a member variable
                userRealm = realm
                getPlants(realm)
            }
        })
    }

    private fun getPlants(realm: Realm) {
        plantsList = realm.where<Plant>().findAll()

        val plantsChangeListener =
            OrderedRealmCollectionChangeListener<RealmResults<Plant>> { updatedResult: RealmResults<Plant>, _: OrderedCollectionChangeSet ->
                Log.i(TAG(), "Fetched new plants data.")
                plants = updatedResult.freeze()
            }

        plantsList.addChangeListener(plantsChangeListener)
    }

    override fun onCleared() {
        super.onCleared()
        plantsList.removeAllChangeListeners()
    }
}