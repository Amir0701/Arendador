package com.example.tenant.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity
data class Tenant(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    @ColumnInfo("first_name")
    val firstName: String,
    @ColumnInfo("last_name")
    val lastName: String,
    @ColumnInfo("date_of_birth")
    val dateOfBirth: Date?,
    @ColumnInfo("passport_number")
    val passportNumber: String,
    @ColumnInfo("phone_number")
    var phoneNumber: String
): java.io.Serializable
