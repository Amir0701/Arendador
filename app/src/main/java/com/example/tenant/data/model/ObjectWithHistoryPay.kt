package com.example.tenant.data.model

import androidx.room.Embedded
import androidx.room.Relation

data class ObjectWithHistoryPay(
    @Embedded val obbject: Obbject,

    @Relation(
        parentColumn = "id",
        entityColumn = "object_id"
    )
    val historyPay: List<HistoryPay>
)
