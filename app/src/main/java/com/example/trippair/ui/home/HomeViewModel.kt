package com.example.trippair.ui.home

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.toUpperCase
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.trippair.db.UserProfile
import com.example.trippair.models.HomeModel
import com.example.trippair.models.TravelModel
import com.example.trippair.models.User
import com.google.firebase.firestore.Filter
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.sql.Timestamp
import java.util.Date
import java.util.Locale


//       SISTEMARE LE IMMAGINI E LA QUERY TOGLIENDO I PROPRI VIAGGI


class HomeViewModel : ViewModel() {
    private val _homeState = MutableStateFlow(HomeModel())
    val homeState: StateFlow<HomeModel> = _homeState.asStateFlow()
    var author_email by mutableStateOf("")
    var author_phone by mutableStateOf("")
    var author_reputation by mutableStateOf(0f)
    val db = Firebase.firestore

    fun initHomeState() {
        viewModelScope.launch {
            db.collection("travels").limit(7)
                .get()
                .addOnSuccessListener { result ->
                    val travelsDB = mutableListOf<TravelModel>()
                    for (document in result) {

                        val travel = TravelModel(
                            id = document.data.get("id").toString(),
                            title = document.data.get("title").toString(),
                            stateDestination = document.data.get("stateDestination").toString(),
                            citiesDestination = document.data.get("citiesDestination") as List<String>,
                            description = document.data.get("description").toString(),
                            startDate = (document.data.get("startDate") as com.google.firebase.Timestamp).toDate(),
                            endDate = (document.data.get("endDate") as com.google.firebase.Timestamp).toDate(),
                            budget = document.data.get("budget") as Double,
                            author = User(
                                id = document.data.get("author_id").toString(),
                                name = document.data.get("author_name").toString(),
                                surname = document.data.get("author_surname").toString(),
                                profileImageUrl = document.data.get("author_profileImageUrl")
                                    .toString(),
                                email = document.data.get("author_email").toString(),
                                phone = document.data.get("author_phone").toString(),
                                reputation = (document.data.get("author_reputation")?.toString()
                                    ?.toFloat() ?: 0f),
                            ),
                        )
                        if (travel.author.id != "") {
                            Log.d("gigi", "${travel}")
                        }
                        travelsDB.add(travel)
                    }
                    _homeState.value = _homeState.value.copy(travels = travelsDB)
                }
                .addOnFailureListener { exception ->
                    Log.w("gigi", "Error getting documents.", exception)
                }

        }
    }

    fun searchTravel(dest: String) {
        viewModelScope.launch {
            db.collection("travels").where(
                Filter.or(
                    Filter.arrayContains("citiesDestination", dest.toUpperCase(Locale.ITALY)),
                    Filter.equalTo("stateDestination", dest.toUpperCase(Locale.ITALY))
                )
            )
                .get()
                .addOnSuccessListener { result ->
                    val travelsDB = mutableListOf<TravelModel>()
                    for (document in result) {

                        val travel = TravelModel(
                            id = document.data.get("id").toString(),
                            title = document.data.get("title").toString(),
                            stateDestination = document.data.get("stateDestination").toString(),
                            citiesDestination = document.data.get("citiesDestination") as List<String>,
                            description = document.data.get("description").toString(),
                            startDate = (document.data.get("startDate") as com.google.firebase.Timestamp).toDate(),
                            endDate = (document.data.get("endDate") as com.google.firebase.Timestamp).toDate(),
                            budget = document.data.get("budget") as Double,
                            author = User(
                                id = document.data.get("author_id").toString(),
                                name = document.data.get("author_name").toString(),
                                surname = document.data.get("author_surname").toString(),
                                profileImageUrl = document.data.get("author_profileImageUrl")
                                    .toString(),
                                email = document.data.get("author_email").toString(),
                                phone = document.data.get("author_phone").toString(),
                                reputation = (document.data.get("author_reputation")?.toString()
                                    ?.toFloat() ?: 0f),
                            ),
                        )
                        if (travel.author.id != "") {
                            Log.d("gigi", "${travel}")
                        }
                        travelsDB.add(travel)
                    }
                    _homeState.value = _homeState.value.copy(travels = travelsDB)
                }
                .addOnFailureListener { exception ->
                    Log.w("gigi", "Error getting documents.", exception)
                }

        }
    }

}