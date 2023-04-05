package com.example.tenant.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import java.util.Date

@Entity
data class Contract(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    @ColumnInfo("object_id")
    val objectId: Int,
    @ColumnInfo("tenant_id")
    val tenantId: Int,
    var sum: Int,
    @ColumnInfo("date_of_contract")
    val dateOfContract: Date,
    @ColumnInfo("date_of_end")
    var dateOfEnd: Date,
    @ColumnInfo("pay_time")
    var timeOfPay: PayTime,
    var zalog: Int?,
    var status: ContractStatus
)
