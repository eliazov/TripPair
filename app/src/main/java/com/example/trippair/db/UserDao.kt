package com.example.trippair.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Upsert

@Dao
interface UserDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: UserProfile): Long

    @Query("DELETE FROM UserProfile")
    suspend fun deleteAll()

    @Query("SELECT * FROM UserProfile WHERE id = :id")
    fun getUserById(id: String): UserProfile?

    @Query("SELECT * FROM UserProfile")
    fun getAllUsers(): UserProfile?

   
}