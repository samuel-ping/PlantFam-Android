package com.example.plantrack_android.manageplantsscreen

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.plantrack_android.model.Plant
import java.time.LocalDate

class ManagePlantsViewModel : ViewModel() {
    private val plants = mutableListOf<Plant>() // TODO: initialize to whatever is stored in db
    private val _plants = MutableLiveData<MutableList<Plant>>()

    fun addPlant(
        nickname: String,
        commonName: String?,
        scientificName: String,
        adoptionDate: LocalDate,
        adoptedFrom: String,
        isDeceased: Boolean,
        deceasedDate: LocalDate?,
        parent: Plant?
    ) {
        plants.add(
            Plant(
                nickname = nickname,
                commonName = commonName,
                scientificName = scientificName,
                adoptionDate = adoptionDate,
                adoptedFrom = adoptedFrom,
                isDeceased = isDeceased,
                deceasedDate = deceasedDate,
                parent = parent
            )
        )
        _plants.value = plants
    }
}