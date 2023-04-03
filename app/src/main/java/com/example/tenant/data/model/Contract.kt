package com.example.tenant.data.model

import java.util.Date

data class Contract(
    val id: Int,
    val objectId: Int,
    val tenantId: Int,
    var sum: Int,
    val dateOfContract: Date,
    var dateOfEnd: Date,
    var timeOfPay: PayTime,
    var zalog: Int?,
    var status: ContractStatus
)
