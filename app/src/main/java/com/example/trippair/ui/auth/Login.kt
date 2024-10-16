package com.example.trippair.ui.auth

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import androidx.room.Room
import com.example.trippair.R
import com.example.trippair.TripPairApp
import com.example.trippair.db.UserProfile
import com.example.trippair.db.UserProfileDB
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

fun loggedIn(username: String, password: String, navController: NavController, context: Context) {
    val db = Firebase.firestore
    db.collection("utenti").whereEqualTo("username", username).whereEqualTo("password", password)
        .get()
        .addOnSuccessListener { documentReference ->
            if (!documentReference.documents.isEmpty()) {
                val sharedPreferences: SharedPreferences =
                    context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
                val editor: SharedPreferences.Editor = sharedPreferences.edit()
                editor.putString(
                    "user_id",
                    documentReference.documents.get(0)?.data?.get("id")?.toString()
                )
                editor.apply()
                val userId = sharedPreferences.getString("user_id", "")
                Log.d("gigi", "userId: $userId")
                insertInRoom(
                    UserProfile(
                        id = documentReference.documents.get(0)?.data?.get("id")?.toString() ?: "",
                        name = documentReference.documents.get(0)?.data?.get("name")?.toString()
                            ?: "",
                        surname = documentReference.documents.get(0)?.data?.get("surname")
                            ?.toString() ?: "",
                        email = documentReference.documents.get(0)?.data?.get("email")?.toString()
                            ?: "",
                        username = documentReference.documents.get(0)?.data?.get("username")
                            ?.toString() ?: "",
                        password = documentReference.documents.get(0)?.data?.get("password")
                            ?.toString() ?: "",
                        phone = documentReference.documents.get(0)?.data?.get("phone")?.toString()
                            ?: "",
                        description = documentReference.documents.get(0)?.data?.get("description")
                            ?.toString() ?: "",
                        age = documentReference.documents.get(0)?.data?.get("age")?.toString()
                            ?.toInt() ?: 0,
                        gender = documentReference.documents.get(0)?.data?.get("gender")?.toString()
                            ?: ""
                    ), context
                )


                navController.popBackStack()
                navController.navigate("home")
            }

        }
        .addOnFailureListener { e ->
            Log.d("gigi", "Error adding document")
        }
}

fun insertInRoom(user: UserProfile, context: Context) {
    val dbRoom = Room.databaseBuilder(
        context,
        UserProfileDB::class.java, "user"
    ).build()
    val userDao = dbRoom.userDao()
    CoroutineScope(Dispatchers.IO).launch {
        userDao.insertUser(user)
        Log.d("gigi", "inserito in room ${userDao.getAllUsers()}")
    }

}

fun isUserLoggedIn(context: Context): Boolean {
    val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
    val userId = sharedPreferences.getString("user_id", "")
    return userId != ""
}

@Composable
fun Login(
    mainController: NavController,
) {
    val context = LocalContext.current
    if (isUserLoggedIn(context = context)) {
        TripPairApp(mainController = mainController)
    } else {
        LoginC(mainController, context = context)
    }
}

@Composable
fun LoginC(navController: NavController, context: Context) {

    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(id = R.drawable.screenshot_2024_09_23_152929),
            contentDescription = "fotoTrip",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(70.dp))
            Box(
                modifier = Modifier
            ) {
                Text(
                    text = "TripPair",
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .padding(5.dp)
                        .align(Alignment.Center),
                    style = TextStyle(
                        brush = Brush.horizontalGradient(
                            listOf(
                                Color(0xFF008EDC),
                                Color(0xFFD30F64)
                            )
                        ),
                        fontSize = 70.sp,
                        letterSpacing = 1.sp,
                        shadow = Shadow(Color.White, Offset(0f, 3f), 15f)
                    ),


                    )
                // }
            }
        }

        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Bottom
        ) {
            TextField(
                value = username, onValueChange = { username = it },
                modifier = Modifier
                    .clip(shape = MaterialTheme.shapes.extraLarge)
                    .wrapContentHeight(),
                colors = TextFieldDefaults.colors(
                    unfocusedIndicatorColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent
                ),
                placeholder = { Text(text = "Username") },
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
            )
            Spacer(modifier = Modifier.height(10.dp))

            TextField(
                value = password,
                onValueChange = { password = it },
                modifier = Modifier
                    .clip(shape = MaterialTheme.shapes.extraLarge)

                    .wrapContentHeight(),
                colors = TextFieldDefaults.colors(
                    unfocusedIndicatorColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent
                ),
                placeholder = { Text(text = "Password") },
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions(onDone = {
                    loggedIn(
                        username,
                        password,
                        navController,
                        context
                    )
                })
            )
            Spacer(modifier = Modifier.height(60.dp))
            Button(
                onClick = { loggedIn(username, password, navController, context) },
                modifier = Modifier
                    .width(250.dp)
                    .shadow(elevation = 5.dp, shape = MaterialTheme.shapes.extraLarge)
                    .height(40.dp)
            ) {
                Text(text = "Log In")
            }
            Spacer(modifier = Modifier.height(10.dp))
            Button(
                onClick = { navController.navigate("register") },
                modifier = Modifier
                    .width(250.dp)
                    .shadow(elevation = 5.dp, shape = MaterialTheme.shapes.extraLarge)
                    .height(40.dp)
            )
            {
                Text(text = "Register")
            }
            Spacer(modifier = Modifier.height(80.dp))

        }
    }
}
