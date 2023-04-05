package com.example.tenant.data.model

import androidx.room.Embedded
import androidx.room.Relation

data class ObjectWithContracts(
    @Embedded val obbject: Obbject,
    @Relation(
        parentColumn = "id",
        entityColumn = "objectId"
    )
    val contracts: List<Contract>
)
