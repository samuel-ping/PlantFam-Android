package com.example.plantrack_android.model

import java.util.*

data class Plant (
    val nickname: String,
    val commonName: String,
    val scientificName: String,
    val adoptionDate: Date,
    val adoptedFrom: String,
    val isDeceased: Boolean,
    val parent: Plant?,
    // Keep track of photos of plant
)