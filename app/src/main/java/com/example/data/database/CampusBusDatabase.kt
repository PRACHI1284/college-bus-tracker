package com.example.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.data.dao.PaymentDao
import com.example.data.dao.NotificationDao
import com.example.data.dao.UserDao
import com.example.data.entity.PaymentEntity
import com.example.data.entity.NotificationEntity
import com.example.data.entity.UserEntity

@Database(
    entities = [PaymentEntity::class, NotificationEntity::class, UserEntity::class],
    version = 1,
    exportSchema = false
)
abstract class CampusBusDatabase : RoomDatabase() {
    abstract fun paymentDao(): PaymentDao
    abstract fun notificationDao(): NotificationDao
    abstract fun userDao(): UserDao

    companion object {
        @Volatile
        private var INSTANCE: CampusBusDatabase? = null

        fun getDatabase(context: Context): CampusBusDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    CampusBusDatabase::class.java,
                    "campus_bus_database"
                )
                .fallbackToDestructiveMigration()
                .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
