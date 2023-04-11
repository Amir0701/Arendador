package com.example.tenant.data.model

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.PrimaryKey
import androidx.room.Relation
import java.util.*

data class ContractWithTenant(
    val id: Int,
    var objectId: Int,
    var tenantId: Int,
    var sum: Int,
    val dateOfContract: Date,
    var dateOfEnd: Date,
    var timeOfPay: PayTime,
    var zalog: Int?,
    var status: ContractStatus,
    val firstName: String,
    val lastName: String,
    val dateOfBirth: Date?,
    val passportNumber: String,
    var phoneNumber: String
)