package com.example.tenant.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity
data class HistoryPay(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    @ColumnInfo("object_id")
    val objectId: Int,
    var sum: Int,
    @ColumnInfo("contract_id")
    val contractId: Int,
    @ColumnInfo("date_of_pay")
    val dateOfPay: Date
)
