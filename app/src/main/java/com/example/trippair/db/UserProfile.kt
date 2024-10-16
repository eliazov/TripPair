package com.example.trippair.db

import androidx.annotation.DrawableRes
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.trippair.R

@Entity(tableName = "UserProfile")
data class UserProfile(
    @PrimaryKey
    var id: String = "",
    var name: String = "",
    var surname: String = "",
    var email: String = "",
    var username: String = "",
    var password: String = "",
    var phone: String = "",
    var description: String = "",
    var gender: String = "",
    var age: Int = 0,
    var profileImageUrl: String = "",
    var reputation: Float = 0f,

    )
