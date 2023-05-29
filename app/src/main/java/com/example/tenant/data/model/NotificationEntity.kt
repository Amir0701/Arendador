package com.example.tenant.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity
data class NotificationEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val title: String,
    val message: String,
    val date: Date,
    val isChecked: Boolean
)