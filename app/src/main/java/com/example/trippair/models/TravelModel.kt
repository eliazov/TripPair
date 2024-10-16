package com.example.trippair.models

import com.example.trippair.db.UserProfile
import java.util.Date


data class TravelModel(
    var id: String = "",

    var title: String = "",
    var stateDestination: String = "",
    var citiesDestination: List<String> = listOf(),
    var description: String = "",
    var startDate: Date = Date(),
    var endDate: Date = Date(),
    var budget: Double = 0.0,
    var transport: String = "",
    var tags: List<String> = listOf(),

    var images: List<String> = listOf(),
    var rating: Float = 0f,
    var participants: List<String> = listOf(),
    var author: User = User(),
    var completed: Boolean = false,
)