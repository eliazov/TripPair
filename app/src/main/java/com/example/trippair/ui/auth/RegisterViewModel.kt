package com.example.trippair.ui.auth

import android.content.Context
import android.content.SharedPreferences
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

class RegisterViewModel : ViewModel() {

    private val _regState = MutableStateFlow(UserProfile())
    val regState: StateFlow<UserProfile> = _regState.asStateFlow()
    val db = Firebase.firestore

    fun onNameChange(name: String) {
        _regState.value = _regState.value.copy(name = name)
    }

    fun onSurnameChange(surname: String) {
        _regState.value = _regState.value.copy(surname = surname)
    }

    fun onEmailChange(email: String) {
        _regState.value = _regState.value.copy(email = email)
    }

    fun onUsernameChange(username: String) {
        _regState.value = _regState.value.copy(username = username)
    }

    fun onPasswordChange(password: String) {
        _regState.value = _regState.value.copy(password = password)
    }

    fun onPhoneChange(phone: String) {
        _regState.value = _regState.value.copy(phone = phone)
    }

    fun onDescriptionChange(description: String) {
        _regState.value = _regState.value.copy(description = description)
    }

    fun onGenderChange(gender: String) {
        _regState.value = _regState.value.copy(gender = gender)
    }

    fun onAgeChange(age: Int) {
        _regState.value = _regState.value.copy(age = age)
    }

    fun createId() {
        _regState.value = _regState.value.copy(
            id = _regState.value.hashCode().toString().plus(_regState.value.email)
        )
    }

    fun saveDataOnRoom(userDao: UserDao, context: Context) {
        CoroutineScope(Dispatchers.IO).launch {
            userDao.insertUser(_regState.value)
            val sharedPreferences: SharedPreferences =
                context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
            val editor: SharedPreferences.Editor = sharedPreferences.edit()


            editor.putString("user_id", _regState.value.id)
            editor.apply()
        }

    }

    fun createProfile() {
        val userData = hashMapOf(
            "id" to _regState.value.id,
            "name" to _regState.value.name,
            "surname" to _regState.value.surname,
            "email" to _regState.value.email,
            "username" to _regState.value.username,
            "password" to _regState.value.password,
            "phone" to _regState.value.phone,
            "description" to _regState.value.description,
            "age" to _regState.value.age,
            "gender" to _regState.value.gender
        )
        viewModelScope.launch {
            db.collection("utenti").document(userData["id"].toString())
                .set(userData)
                .addOnSuccessListener {
                    Log.d(
                        "dataFirebase",
                        "DocumentSnapshot successfully written! : ${userData}"
                    )
                }
                .addOnFailureListener { e -> Log.d("dataFirebase", "Error writing document", e) }
        }
    }

}