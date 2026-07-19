package com.example.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "payments")
data class PaymentEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val studentId: String,
    val studentName: String,
    val amount: Double,
    val paymentMethod: String, // "UPI", "Credit Card", "Debit Card", "Net Banking"
    val status: String, // "SUCCESS", "PENDING", "FAILED"
    val timestamp: Long,
    val routeName: String,
    val referenceId: String
)

@Entity(tableName = "notifications")
data class NotificationEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val message: String,
    val timestamp: Long,
    val isRead: Boolean = false,
    val type: String // "ARRIVAL", "DELAY", "PAYMENT", "GENERAL"
)

@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey val studentId: String,
    val fullName: String,
    val email: String,
    val phoneNumber: String,
    val isVerified: Boolean = false,
    val isTwoFactorEnabled: Boolean = true
)
