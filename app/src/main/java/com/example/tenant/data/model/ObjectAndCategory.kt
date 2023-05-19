package com.example.tenant.data.model

import androidx.room.ColumnInfo
import androidx.room.PrimaryKey

data class ObjectAndCategory(
    val id: Int,
    var name: String,
    val categoryId: Int,
    var objectStatus: ObjectStatus,
    var square: Double?,
    val address: String?,
    val categoryName: String,
    val image: String?
): java.io.Serializable
