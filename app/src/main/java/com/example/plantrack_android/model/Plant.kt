package com.example.plantrack_android.model

import java.util.*

data class Plant (
    val nickname: String,
    val scientificName: String,
    val adoptionDate: Date,
    // Keep track of photos of plant
)