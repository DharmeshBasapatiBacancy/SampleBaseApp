package com.bacancy.samplebaseapp.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [TbUser::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
}