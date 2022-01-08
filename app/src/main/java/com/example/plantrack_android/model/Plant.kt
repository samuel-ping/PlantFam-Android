package com.example.plantrack_android.model

import java.time.LocalDate

data class Plant(
    val nickname: String,
    val commonName: String?,
    val scientificName: String,
    val adoptionDate: LocalDate,
    val adoptedFrom: String,
    val isDeceased: Boolean,
    val deceasedDate: LocalDate?,
    val parent: Plant?,
    // Keep track of photos of plant
)