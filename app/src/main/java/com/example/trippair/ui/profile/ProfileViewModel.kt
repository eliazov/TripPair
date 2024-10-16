package com.example.trippair.ui.profile


import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.trippair.db.UserDao

import com.example.trippair.db.UserProfile

import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch


class ProfileViewModel() : ViewModel() {
    private val _userState = MutableStateFlow(UserProfile())
    val userState: StateFlow<UserProfile> = _userState.asStateFlow()
    val db = Firebase.firestore

    fun laodData_user(id: String): Boolean {
        viewModelScope.launch {
            val db = Firebase.firestore
            db.collection("utenti").whereEqualTo("id", id)
                .get()
                .addOnSuccessListener { result ->
                    val temp: UserProfile = _userState.value.copy(
                        name = result.documents.get(0).data?.get("name").toString(),
                        surname = result.documents.get(0).data?.get("surname").toString(),
                        email = result.documents.get(0).data?.get("email").toString(),
                        username = result.documents.get(0).data?.get("username").toString(),
                        password = result.documents.get(0).data?.get("password").toString(),
                        phone = result.documents.get(0).data?.get("phone").toString(),
                        description = result.documents.get(0).data?.get("description").toString(),
                        gender = result.documents.get(0).data?.get("gender").toString(),
                        age = result.documents.get(0).data?.get("age").toString().toInt(),
                        id = id
                    )
                    _userState.value = temp

                }
                .addOnFailureListener { exception ->
                    Log.w("gigi", "Error getting documents.", exception)

                }
        }
        return true
    }

    fun updateProfile(user: UserProfile) {
        _userState.value = user
    }
}