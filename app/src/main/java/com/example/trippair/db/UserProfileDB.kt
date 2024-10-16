package com.example.trippair.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [UserProfile::class], version = 1)
abstract class UserProfileDB : RoomDatabase() {
    abstract fun userDao(): UserDao

    
}