package com.example.tenant.data.model

data class Obbject(
    val id: Int,
    var name: String,
    val categoryId: Int,
    var objectStatus: ObjectStatus,
    var square: Double,
    val address: String
)
