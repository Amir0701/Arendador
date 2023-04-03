package com.example.tenant.data.model

import java.util.Date

data class Tenant(
    val id: Int,
    val firstName: String,
    val lastName: String,
    val dateOfBirth: Date?,
    val passportNumber: String,
    var phoneNumber: String
)
