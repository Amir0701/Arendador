package com.example.tenant.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import javax.annotation.Nullable

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
    val address: String?,
    @Nullable
    var image: ByteArray? = null
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Obbject

        if (id != other.id) return false
        if (name != other.name) return false
        if (categoryId != other.categoryId) return false
        if (objectStatus != other.objectStatus) return false
        if (square != other.square) return false
        if (address != other.address) return false
        if (image != null) {
            if (other.image == null) return false
            if (!image.contentEquals(other.image)) return false
        } else if (other.image != null) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id
        result = 31 * result + name.hashCode()
        result = 31 * result + categoryId
        result = 31 * result + objectStatus.hashCode()
        result = 31 * result + (square?.hashCode() ?: 0)
        result = 31 * result + (address?.hashCode() ?: 0)
        result = 31 * result + (image?.contentHashCode() ?: 0)
        return result
    }
}
