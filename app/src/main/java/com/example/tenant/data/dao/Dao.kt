package com.example.tenant.data.dao

import androidx.room.*
import com.example.tenant.data.model.*

@androidx.room.Dao
interface Dao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addObject(obbject: Obbject)

    @Query("SELECT * FROM Obbject WHERE id = :objId")
    suspend fun getObjectById(objId: Int): Obbject

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addCategory(category: Category)

    @Query("SELECT * FROM Category")
    suspend fun getAllCategories(): List<Category>

    @Query("SELECT * FROM Obbject")
    suspend fun getAllObjects(): List<Obbject>

    @Query("SELECT obbject.id, obbject.name, obbject.category_id AS categoryId, " +
            "obbject.status as objectStatus, obbject.square, obbject.address, category.name AS categoryName " +
            "FROM obbject INNER JOIN category ON obbject.category_id=category.id")
    suspend fun getAllObjectsWithCategory(): List<ObjectAndCategory>

    @Query("SELECT * FROM exploitation WHERE object_id = :objectId")
    suspend fun getAllExploitation(objectId: Int): List<Exploitation>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addExploitation(exploitation: Exploitation)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addContract(contract: Contract): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addTenant(tenant: Tenant): Long

    @Query("SELECT * FROM tenant")
    suspend fun getAllTenant(): List<Tenant>

    @Transaction
    @Query("SELECT contract.id, contract.object_id AS objectId, contract.tenant_id as tenantId," +
            " contract.sum, contract.date_of_contract as dateOfContract," +
            "contract.date_of_end as dateOfEnd, contract.pay_time as timeOfPay, contract.zalog, contract.status, " +
            "tenant.first_name AS firstName, tenant.last_name as lastName, " +
            "tenant.date_of_birth as dateOfBirth, tenant.passport_number as passportNumber, " +
            "tenant.phone_number as phoneNumber FROM contract INNER JOIN tenant ON contract.tenant_id = tenant.id " +
            "WHERE contract.object_id = :objectId")
    suspend fun getContractWithTenant(objectId: Int): List<ContractWithTenant>

    @Query("DELETE FROM obbject WHERE obbject.id = :id")
    suspend fun deleteObject(id: Int): Int

    @Query("DELETE FROM contract WHERE contract.object_id = :id")
    suspend fun deleteContractByObjectId(id: Int)

    @Query("DELETE FROM exploitation WHERE exploitation.object_id = :id")
    suspend fun deleteExploitationByObjectId(id: Int)

    @Delete
    suspend fun deleteExploitation(exploitation: Exploitation)

    @Query("UPDATE obbject SET status = :objectStatus WHERE id = :id")
    suspend fun updateObjectStatus(id: Int, objectStatus: ObjectStatus)

}