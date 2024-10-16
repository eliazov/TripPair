package com.example.trippair.ui.profile

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import androidx.room.Room
import com.example.trippair.R
import com.example.trippair.db.UserProfile
import com.example.trippair.db.UserProfileDB
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.SwipeRefreshIndicator
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


//       DA IMPLEMENTARE LA PARTE DI ELIMINA PROFILO FATTA BENE E LA PARTE DI MODIFICA E DIARIO DI VIAGGIO

data class Pro(val title: String)

val travels = listOf(
    Pro("Belgio"), Pro("Francia"), Pro("India"), Pro("Canada"), Pro("Italia")
)

@Composable
fun Profile(
    onClick: () -> Unit = {},
    mainController: NavController,
    userViewModel: ProfileViewModel = viewModel(),
) {
    val context = LocalContext.current
    val userUiState by userViewModel.userState.collectAsState()

    val dbRoom =
        Room.databaseBuilder(
            context,
            UserProfileDB::class.java, "user"
        ).build()
    val userDao = dbRoom.userDao()

    val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
    val userId = sharedPreferences.getString("user_id", "")

    LaunchedEffect(true) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                if (userDao.getUserById(userId.toString()) == null) {
                    userViewModel.laodData_user(userId.toString())
                } else {
                    userViewModel.updateProfile(
                        userDao.getUserById(userId.toString()) ?: UserProfile()
                    )
                }
            } catch (e: Exception) {
                Log.d("database", "Errore: ${e.message}")
                userViewModel.laodData_user(userId.toString())
            }

        }
    }
    val bg_c = MaterialTheme.colorScheme.onPrimary

    var expanded by remember { mutableStateOf(false) }
    var isRefreshing by remember { mutableStateOf(false) }


    val swipeRefreshState = rememberSwipeRefreshState(isRefreshing)
    LaunchedEffect(isRefreshing) {
        if (isRefreshing) {
            userViewModel.laodData_user(userId.toString())
            delay(200)
            isRefreshing = false
        }
    }

    SwipeRefresh(
        state = swipeRefreshState,
        onRefresh = {
            isRefreshing = true

        },
        indicator = { state, refreshTrigger ->
            SwipeRefreshIndicator(
                state = state,
                refreshTriggerDistance = refreshTrigger,
                scale = true
            )
        }
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.onPrimary)
                .verticalScroll(ScrollState(0))
        ) {
            Column {
                Box {
                    Image(
                        painter = painterResource(id = R.drawable.img_20240320_wa0009),
                        contentDescription = "Profile Picture",
                        modifier = Modifier
                            .drawWithContent {
                                this.drawContent()
                                this.drawRect(
                                    brush = Brush.verticalGradient(
                                        0.0f to Color.Transparent,
                                        1.0f to bg_c,
                                        startY = size.height * 0.65f,
                                        endY = size.height
                                    ),
                                    style = Fill
                                )
                            }
                            .height(250.dp)
                            .fillMaxWidth(), contentScale = ContentScale.Crop
                    )
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentSize(Alignment.TopEnd)
                            .padding(8.dp)
                    ) {
                        IconButton(
                            onClick = { expanded = true },
                            modifier = Modifier
                                .align(Alignment.TopEnd)
                                .size(35.dp),
                            colors = IconButtonDefaults.iconButtonColors(
                                containerColor = MaterialTheme.colorScheme.onPrimary,
                                contentColor = MaterialTheme.colorScheme.primary
                            )
                        ) {
                            Icon(
                                imageVector = Icons.Filled.MoreVert,
                                contentDescription = "",
                                modifier = Modifier.size(30.dp),
                            )
                        }
                        DropdownMenu(
                            expanded = expanded,
                            onDismissRequest = { expanded = false },
                        ) {
                            DropdownMenuItem(
                                text = { Text("Log Out") },
                                onClick = {
                                    Toast.makeText(context, "Log out", Toast.LENGTH_SHORT).show()
                                    CoroutineScope(Dispatchers.IO).launch {
                                        userDao.deleteAll()
                                    }
                                    val editor: SharedPreferences.Editor = sharedPreferences.edit()
                                    editor.remove("user_id")
                                    editor.apply()
                                    mainController.navigate("login") {
                                        popUpTo("home") {
                                            inclusive = true
                                        }
                                    }
                                }
                            )
                            DropdownMenuItem(
                                text = {
                                    Text(
                                        "Elimina Profilo",
                                        color = MaterialTheme.colorScheme.error
                                    )
                                },
                                onClick = {
                                    Toast.makeText(
                                        context,
                                        "Profilo Eliminato",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    //chiamata firebase per eliminare il profilo
                                    CoroutineScope(Dispatchers.IO).launch {
                                        userDao.deleteAll()
                                    }
                                }
                            )
                        }
                    }
                }

                Spacer(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(20.dp)
                )
                Text(
                    text = userUiState.name + " " + userUiState.surname,
                    color = MaterialTheme.colorScheme.primary,
                    fontSize = 36.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(start = 30.dp)
                )
                Spacer(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(20.dp)
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Card(
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.primaryContainer,
                            contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                        ),
                        modifier = Modifier
                            .width(120.dp)
                            .height(120.dp)
                            .padding(4.dp)
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.fillMaxSize(),
                            verticalArrangement = Arrangement.SpaceAround
                        ) {
                            Text(
                                text = "3",
                                fontWeight = FontWeight.Medium,
                                fontSize = 30.sp,
                                modifier = Modifier.padding(top = 6.dp)
                            )
                            Text(
                                text = "Paesi \nvisitati",
                                fontWeight = FontWeight.Medium,
                                fontSize = 15.sp,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.padding(bottom = 6.dp)
                            )
                        }
                    }
                    Card(
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.primaryContainer,
                            contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                        ),
                        modifier = Modifier
                            .width(120.dp)
                            .height(120.dp)
                            .padding(4.dp)
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.fillMaxSize(),
                            verticalArrangement = Arrangement.SpaceAround
                        ) {
                            Text(
                                text = "2",
                                fontWeight = FontWeight.Medium,
                                fontSize = 30.sp,
                                modifier = Modifier.padding(top = 6.dp)
                            )
                            Text(
                                text = "Viaggi \ncompletati",
                                fontWeight = FontWeight.Medium,
                                fontSize = 15.sp,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.padding(bottom = 6.dp)
                            )
                        }
                    }
                    Card(
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.primaryContainer,
                            contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                        ),
                        modifier = Modifier
                            .width(120.dp)
                            .height(120.dp)
                            .padding(4.dp)
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.fillMaxSize(),
                            verticalArrangement = Arrangement.SpaceAround
                        ) {
                            Row(
                                modifier = Modifier.padding(top = 6.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "4.5",
                                    fontWeight = FontWeight.Medium,
                                    fontSize = 30.sp,
                                )
                                Icon(
                                    imageVector = Icons.Filled.Star,
                                    contentDescription = "",
                                    modifier = Modifier.size(30.dp)
                                )
                            }

                            Text(
                                text = "Valutazione \ntotale",
                                fontWeight = FontWeight.Medium,
                                fontSize = 15.sp,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.padding(bottom = 6.dp)
                            )
                        }
                    }
                }



                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 30.dp, end = 30.dp, top = 15.dp, bottom = 15.dp)
                ) {
                    Text(
                        text = "Vai al diario di viaggio",
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp)
                            .padding(8.dp)
                            .clickable { onClick() },
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.primaryContainer,
                            contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                        ),
                        elevation = CardDefaults.cardElevation(
                            defaultElevation = 6.dp
                        )
                    ) {
                        LazyVerticalGrid(
                            columns = GridCells.Fixed(2),
                            modifier = Modifier.padding(bottom = 8.dp)
                        ) {
                            items(travels) { travel ->
                                Card(
                                    modifier = Modifier
                                        .padding(8.dp)
                                        .height(50.dp), colors = CardDefaults.cardColors(
                                        containerColor = MaterialTheme.colorScheme.onPrimary,
                                        contentColor = MaterialTheme.colorScheme.primary
                                    )
                                ) {
                                    Row(
                                        modifier = Modifier
                                            .padding(4.dp)
                                            .fillMaxSize(),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Icon(
                                            painter = painterResource(id = R.drawable.flag_48px),
                                            contentDescription = ""
                                        )
                                        Text(text = travel.title)
                                    }

                                }
                            }
                        }

                    }
                }


                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 30.dp, end = 30.dp, top = 15.dp, bottom = 15.dp)
                ) {
                    Text(
                        text = "Modifica il profilo",
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            //.height(200.dp)
                            .padding(8.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.primaryContainer,
                            contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                        ),
                        elevation = CardDefaults.cardElevation(
                            defaultElevation = 6.dp
                        )
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                text = "Descrizione",
                                fontWeight = FontWeight.Medium,
                                modifier = Modifier.padding(start = 8.dp, bottom = 2.dp)
                            )
                            Card(
                                modifier = Modifier, colors = CardDefaults.cardColors(
                                    containerColor = MaterialTheme.colorScheme.onPrimary,
                                    contentColor = MaterialTheme.colorScheme.primary
                                )
                            ) {
                                Text(
                                    text = userUiState.description,
                                    modifier = Modifier.padding(6.dp)
                                )
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "Hobby",
                                fontWeight = FontWeight.Medium,
                                modifier = Modifier.padding(start = 8.dp, bottom = 2.dp)
                            )
                            Row {
                                Card(
                                    modifier = Modifier.padding(end = 4.dp),
                                    colors = CardDefaults.cardColors(
                                        containerColor = MaterialTheme.colorScheme.onPrimary,
                                        contentColor = MaterialTheme.colorScheme.primary
                                    )
                                ) {
                                    Text(
                                        text = "Sport",
                                        modifier = Modifier.padding(6.dp)
                                    )
                                }
                                Card(
                                    modifier = Modifier.padding(end = 4.dp),
                                    colors = CardDefaults.cardColors(
                                        containerColor = MaterialTheme.colorScheme.onPrimary,
                                        contentColor = MaterialTheme.colorScheme.primary
                                    )
                                ) {
                                    Text(
                                        text = "Lettura",
                                        modifier = Modifier.padding(6.dp)
                                    )
                                }
                                Card(
                                    modifier = Modifier.padding(end = 4.dp),
                                    colors = CardDefaults.cardColors(
                                        containerColor = MaterialTheme.colorScheme.onPrimary,
                                        contentColor = MaterialTheme.colorScheme.primary
                                    )
                                ) {
                                    Text(
                                        text = "Sport",
                                        modifier = Modifier.padding(6.dp)
                                    )
                                }
                            }


                        }

                    }
                }


            }
        }
    }
}


