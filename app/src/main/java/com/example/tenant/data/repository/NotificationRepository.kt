package com.example.tenant.data.repository

import com.example.tenant.data.dao.Dao
import com.example.tenant.data.model.NotificationEntity
import com.example.tenant.ioc.scope.NotificationRepositoryScope
import javax.inject.Inject

@NotificationRepositoryScope
class NotificationRepository @Inject constructor(val dao: Dao) {
    suspend fun addNotification(notificationEntity: NotificationEntity) =
        dao.addNotification(notificationEntity)

    suspend fun getAllNotification() = dao.getNotifications()
}