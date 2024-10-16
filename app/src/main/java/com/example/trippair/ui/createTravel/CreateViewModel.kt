package com.example.trippair.ui.createTravel

import android.util.Log
import androidx.compose.ui.text.toUpperCase
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.trippair.db.UserDao
import com.example.trippair.models.TravelModel
import com.example.trippair.models.User
import com.example.trippair.ui.profile.ProfileViewModel
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.Date
import java.util.Locale

class CreateViewModel : ViewModel() {
    private val _createState = MutableStateFlow(TravelModel())
    val createState: StateFlow<TravelModel> = _createState.asStateFlow()

    val db = Firebase.firestore

    fun onTitleChange(title: String) {
        _createState.value = _createState.value.copy(title = title)
    }

    fun onStateDestinationChange(stateDestination: String) {
        _createState.value = _createState.value.copy(stateDestination = stateDestination)
    }

    fun onCitiesDestinationChange(citiesDestination: List<String>) {
        _createState.value = _createState.value.copy(citiesDestination = citiesDestination)
    }

    fun onDescriptionChange(description: String) {
        _createState.value = _createState.value.copy(description = description)
    }

    fun onStartDateChange(startDate: Date?) {
        if (startDate != null)
            _createState.value = _createState.value.copy(startDate = startDate)

    }

    fun onEndDateChange(endDate: Date?) {
        if (endDate != null)
            _createState.value = _createState.value.copy(endDate = endDate)
    }

    fun onBudgetChange(budget: Double) {
        _createState.value = _createState.value.copy(budget = budget)
    }

    fun onTransportChange(transport: String) {
        _createState.value = _createState.value.copy(transport = transport)
    }

    fun onTagsChange(tags: List<String>) {
        _createState.value = _createState.value.copy(tags = tags)
    }

    fun freeTravel() {
        _createState.value = TravelModel()
    }

    fun onCreateTravel(userDao: UserDao, userId: String) {
        var userTemp = User()
        CoroutineScope(Dispatchers.IO).launch {
            try {
                userTemp = User(
                    id = userDao.getUserById(userId)?.id ?: "errore",
                    name = userDao.getUserById(userId)?.name ?: "",
                    surname = userDao.getUserById(userId)?.surname ?: "",
                    profileImageUrl = userDao.getUserById(userId)?.profileImageUrl ?: "",
                    reputation = userDao.getUserById(userId)?.reputation ?: 0f,
                    email = userDao.getUserById(userId)?.email ?: "",
                    phone = userDao.getUserById(userId)?.phone ?: "",
                )

            } catch (e: Exception) {
                Log.d("gigi", "dati Room ${e}")
            }
        }
        while (userTemp.id == "") {
        }
        if (userTemp.id == "errore") {
            return
        }
        _createState.value.citiesDestination.map { it.toUpperCase(Locale.ITALY) }
        _createState.value.author.id = userTemp.id
        _createState.value.author.name = userTemp.name
        _createState.value.author.surname = userTemp.surname
        _createState.value.author.profileImageUrl = userTemp.profileImageUrl
        _createState.value.author.reputation = userTemp.reputation
        _createState.value.author.email = userTemp.email
        _createState.value.author.phone = userTemp.phone
        _createState.value.id =
            _createState.value.hashCode().toString() + _createState.value.author.id
        val travelData = hashMapOf(
            "id" to _createState.value.id,
            "title" to _createState.value.title,
            "description" to _createState.value.description,
            "stateDestination" to _createState.value.stateDestination.toUpperCase(Locale.ITALY),
            "citiesDestination" to _createState.value.citiesDestination,
            "startDate" to _createState.value.startDate,
            "endDate" to _createState.value.endDate,
            "budget" to _createState.value.budget,
            "transport" to _createState.value.transport,
            "tags" to _createState.value.tags,
            "images" to _createState.value.images,
            "rating" to _createState.value.rating,
            "participants" to _createState.value.participants,
            "author_id" to _createState.value.author.id,
            "author_name" to _createState.value.author.name,
            "author_surname" to _createState.value.author.surname,
            "author_profileImageUrl" to _createState.value.author.profileImageUrl,
            "completed" to _createState.value.completed,
            "author_email" to _createState.value.author.email,
            "author_phone" to _createState.value.author.phone,
            "author_reputation" to _createState.value.author.reputation,
        )
        viewModelScope.launch {
            db.collection("travels").document(_createState.value.id)
                .set(travelData)
                .addOnSuccessListener {
                    Log.d(
                        "gigi",
                        "DocumentSnapshot successfully written! : ${travelData}"
                    )
                }
                .addOnFailureListener { e -> Log.d("gigi", "Error writing document", e) }
        }

    }

}