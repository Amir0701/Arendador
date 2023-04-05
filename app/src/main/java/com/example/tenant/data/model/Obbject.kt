package com.example.tenant.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Obbject(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    var name: String,
    @ColumnInfo("category_id")
    val categoryId: Int,
    @ColumnInfo("status")
    var objectStatus: ObjectStatus,
    var square: Double?,
    val address: String?
)
