package com.example.trippair.models

import androidx.compose.foundation.ScrollState

data class HomeModel(
    var travels: MutableList<TravelModel> = mutableListOf(),
    var scrollState: ScrollState = ScrollState(0),
)
