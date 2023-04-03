package com.example.tenant.data.model

import java.util.*

data class HistoryPay(
    val id: Int,
    val objectId: Int,
    var sum: Int,
    val dateOfPay: Date
)
