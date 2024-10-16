package com.example.trippair.ui.auth

import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme

import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.room.Room
import com.example.trippair.db.UserProfileDB

var dir = 1

//TO DO
// 1. Aggiungere la possibilità di caricare un'immagine di profilo
// 2. Fare il check password e non mandare avanti se non è abbastanza sicura
// 3. Fare il check di tutti i campi

@Composable
fun Register(
    mainController: NavController,
    registerViewModel: RegisterViewModel = viewModel(),
) {
    val animateController = rememberNavController()
    NavHost(navController = animateController, startDestination = "screen1") {
        composable(
            "screen1",
            enterTransition = {
                slideInHorizontally(
                    animationSpec = tween(200),
                    initialOffsetX = { -it })
            },
            exitTransition = {
                slideOutHorizontally(animationSpec = tween(200), targetOffsetX = { -it })
            }
        ) {
            FirstDataScreen(
                navController = animateController,
                loginController = mainController,
                registerViewModel
            )
        }
        composable(
            "screen2",
            enterTransition = {
                slideInHorizontally(
                    animationSpec = tween(200),
                    initialOffsetX = { dir * it })
            },
            exitTransition = {
                slideOutHorizontally(
                    animationSpec = tween(200),
                    targetOffsetX = { dir * it })
            }
        ) {
            SecondDataScreen(
                navController = animateController,
                viewModel = registerViewModel,
                homeController = mainController
            )
        }
    }

}

fun checkPassword(password: String): Int {
    var count = 0
    if (password.length >= 8) count += 100
    if (password.contains(Regex("[0-9]"))) count += 100
    if (password.contains(Regex("[a-z]"))) count += 50
    return count
}

@Composable
fun FirstDataScreen(
    navController: NavController,
    loginController: NavController,
    viewModel: RegisterViewModel,
) {
    val regUiState by viewModel.regState.collectAsState()

    var security by remember {
        mutableIntStateOf(0)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(80.dp))
        Text(
            text = "Benvenuto/a", fontWeight = FontWeight.Bold,
            style = TextStyle(
                brush = Brush.horizontalGradient(
                    listOf(
                        Color(0xFF008EDC),
                        Color(0xFFD30F64)
                    )
                ),
                fontSize = 40.sp,
                letterSpacing = 1.sp,
                shadow = Shadow(Color.White, Offset(0f, 3f), 15f)
            ),
        )
        Spacer(modifier = Modifier.height(60.dp))
        Box(modifier = Modifier.padding(20.dp)) {
            Card(colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)) {
                Column {
                    Text(
                        text = "questa è una breve descrizione\ndi quello che ci sarà scritto qui",
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
                        fontSize = 18.sp
                    )
                    Row(modifier = Modifier.padding(top = 5.dp)) {
                        Spacer(modifier = Modifier.width(10.dp))
                        OutlinedTextField(
                            value = regUiState.name,
                            onValueChange = { viewModel.onNameChange(it) },
                            modifier = Modifier.weight(0.5f),
                            label = {
                                Text(
                                    text = "Name"
                                )
                            },
                            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        OutlinedTextField(
                            value = regUiState.surname,
                            onValueChange = { viewModel.onSurnameChange(it) },
                            modifier = Modifier.weight(0.5f),
                            label = { Text(text = "Surname") },
                            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                    }
                    OutlinedTextField(
                        value = regUiState.email,
                        onValueChange = { viewModel.onEmailChange(it) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(10.dp),
                        label = { Text(text = "Email") },
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }

            }
        }

        Box(modifier = Modifier.padding(start = 20.dp, end = 20.dp, bottom = 20.dp, top = 0.dp)) {
            Card(colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)) {
                Column(
                    modifier = Modifier
                        .padding(start = 10.dp, end = 10.dp, top = 10.dp, bottom = 18.dp)
                        .fillMaxWidth()
                ) {
                    Text(
                        text = "questa è una breve descrizione\ndi quello che ci sarà scritto qui",
                        fontSize = 18.sp
                    )
                    OutlinedTextField(
                        value = regUiState.username,
                        onValueChange = { viewModel.onUsernameChange(it) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 10.dp),
                        label = {
                            Text(
                                text = "Username"
                            )
                        },
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    OutlinedTextField(
                        value = regUiState.password,
                        onValueChange = {
                            viewModel.onPasswordChange(it)
                            security = checkPassword(it)
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 10.dp),
                        label = { Text(text = "Password") },
                        keyboardOptions = KeyboardOptions(
                            imeAction = ImeAction.Go
                        ),
                        visualTransformation = PasswordVisualTransformation(),
                        keyboardActions = KeyboardActions(onGo = {
                            if (security >= 200) {
                                navController.navigate("screen2")
                            }
                        })


                    )



                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(shape = MaterialTheme.shapes.large)
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(Color.White)
                        ) {
                            Spacer(
                                modifier = Modifier
                                    .height(20.dp)
                                    .width(security.dp)
                                    .background(Color.Red)
                            )
                        }
                    }

                }

            }
        }
        Row(
            horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 30.dp)
        ) {
            Button(onClick = { loginController.navigate("login") }, modifier = Modifier) {

                Text(text = "back to login")
            }
            Button(onClick = { navController.navigate("screen2") }, modifier = Modifier) {

                Text(text = "next")
            }
        }

    }


}

@Composable
fun SecondDataScreen(
    navController: NavController,
    viewModel: RegisterViewModel,
    homeController: NavController,
) {
    // foto
    val regUiState by viewModel.regState.collectAsState()
    val context = LocalContext.current
    val dbRoom =
        Room.databaseBuilder(
            context,
            UserProfileDB::class.java, "user"
        ).build()

    val userDao = dbRoom.userDao()


    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(80.dp))
        Text(
            text = "Benvenuto/a", fontWeight = FontWeight.Bold,
            style = TextStyle(
                brush = Brush.horizontalGradient(
                    listOf(
                        Color(0xFF008EDC),
                        Color(0xFFD30F64)
                    )
                ),
                fontSize = 40.sp,
                letterSpacing = 1.sp,
                shadow = Shadow(Color.White, Offset(0f, 3f), 15f)
            ),
        )
        Spacer(modifier = Modifier.height(60.dp))

        Spacer(modifier = Modifier.height(150.dp))
        Box(
            modifier = Modifier.padding(
                start = 20.dp,
                end = 20.dp,
                bottom = 20.dp,
                top = 0.dp
            )
        ) {
            Card(colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)) {
                Column(
                    modifier = Modifier
                        .padding(start = 10.dp, end = 10.dp, top = 10.dp, bottom = 18.dp)
                        .fillMaxWidth()
                ) {
                    Text(
                        text = "questa è una breve descrizione\ndi quello che ci sarà scritto qui",
                        fontSize = 18.sp
                    )
                    Row(modifier = Modifier.padding(top = 5.dp)) {
                        OutlinedTextField(
                            value = regUiState.gender,
                            onValueChange = { viewModel.onGenderChange(it) },
                            modifier = Modifier.weight(0.5f),
                            placeholder = { Text(text = "M / F / Other") },
                            label = {
                                Text(
                                    text = "Sex"
                                )
                            },
                            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        OutlinedTextField(
                            value = if (regUiState.age.toString() == "0") "" else regUiState.age.toString(),
                            onValueChange = {
                                if (it == "") {
                                    viewModel.onAgeChange(0)
                                } else {
                                    viewModel.onAgeChange(it.toInt())
                                }
                            },
                            modifier = Modifier.weight(0.5f),
                            label = { Text(text = "Age") },
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Number,
                                imeAction = ImeAction.Next
                            ),

                            )

                    }

                    OutlinedTextField(
                        value = regUiState.phone,
                        onValueChange = { viewModel.onPhoneChange(it) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 10.dp),
                        label = {
                            Text(
                                text = "Phone"
                            )
                        },
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    OutlinedTextField(
                        value = regUiState.description,
                        onValueChange = { viewModel.onDescriptionChange(it) },
                        maxLines = 3,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 10.dp)
                            .height(100.dp),
                        label = { Text(text = "Short Description") },

                        )

                }

            }
        }

        Row(
            horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 30.dp)
        ) {
            Button(onClick = { navController.navigate("screen1") }, modifier = Modifier) {

                Text(text = "back")
            }
            Button(onClick = {
                viewModel.createId()
                viewModel.createProfile()
                viewModel.saveDataOnRoom(userDao, context = context)
                homeController.navigate("home")
            }, modifier = Modifier) {

                Text(text = "create")
            }
        }

    }


}


@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PreviewRegister() {
    //SecondDataScreen(navController = rememberNavController(), viewModel())
}