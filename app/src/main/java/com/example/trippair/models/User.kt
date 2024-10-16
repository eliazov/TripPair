package com.example.trippair.models

import androidx.annotation.DrawableRes
import com.example.trippair.R

data class User(
    var id: String = "",

    var name: String = "",
    var surname: String = "",
    var profileImageUrl: String = "",
    var reputation: Float = 0f,
    var email: String = "",
    var phone: String = "",

    )
