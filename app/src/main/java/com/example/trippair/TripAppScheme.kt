package com.example.trippair


import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Person

import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.trippair.models.TravelModel
import com.example.trippair.ui.createTravel.CreateTravel
import com.example.trippair.ui.createTravel.CreateViewModel
import com.example.trippair.ui.home.HomeViewModel
import com.example.trippair.ui.profile.Profile
import com.example.trippair.ui.profile.ProfileViewModel
//import com.example.trippair.ui.home.components.TravelDetail
import com.example.trippair.ui.profile.TravelDiary
import com.example.trippair.ui.home.WorldMapStart
import com.example.trippair.ui.home.components.TravelDetail

enum class TripAppScheme {
    WorldMap,
    CreateTravel,
    Profile,
    TravelDetail,
    Diary,
    Login,
    Register
}

@Composable
fun BottomBarApp(navController: NavHostController = rememberNavController()) {
    var selectedItem by remember { mutableIntStateOf(0) }

    NavigationBar {
        NavigationBarItem(
            icon = {
                Icon(
                    painterResource(id = R.drawable.home_icon),
                    contentDescription = "Home"
                )
            },
            label = { Text("Home") },
            selected = selectedItem == 0,
            onClick = {
                selectedItem = 0
                navController.popBackStack()
                navController.navigate(TripAppScheme.WorldMap.name)
            }
        )
        NavigationBarItem(
            icon = { Icon(Icons.Filled.AddCircle, contentDescription = "Create") },
            label = { Text("Create") },
            selected = selectedItem == 1,
            onClick = {
                selectedItem = 1
                navController.popBackStack()
                navController.navigate(TripAppScheme.CreateTravel.name)
            }
        )
        NavigationBarItem(
            icon = { Icon(Icons.Filled.Person, contentDescription = "Profile") },
            label = { Text("Profile") },
            selected = selectedItem == 2,
            onClick = {
                selectedItem = 2
                navController.popBackStack()
                navController.navigate(TripAppScheme.Profile.name)
            }
        )
    }
    /*
        var currentScreen by remember {
            mutableStateOf(TripAppScheme.WorldMap)
        }
        Column {
            Divider(
                modifier = Modifier.height(0.5.dp),
                color = MaterialTheme.colorScheme.primaryContainer
            )

            BottomAppBar(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(55.dp),
                containerColor = MaterialTheme.colorScheme.onPrimary,
            ) {
                IconButton(onClick = {
                    navController.navigate(TripAppScheme.WorldMap.name)
                    currentScreen = TripAppScheme.WorldMap
                }, modifier = Modifier.weight(1f)) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Bottom
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.home_icon),
                            tint = MaterialTheme.colorScheme.onPrimaryContainer,
                            contentDescription = "home",
                            modifier = Modifier
                                .size(35.dp)
                                .clip(RoundedCornerShape(8.dp))

                        )
                        Divider(
                            modifier = Modifier
                                .height(8.dp)
                                .width(25.dp)
                                .clip(RoundedCornerShape(8.dp)),
                            color = if (currentScreen == TripAppScheme.WorldMap) MaterialTheme.colorScheme.onPrimaryContainer else Color(
                                0x00FFFFFF
                            )
                        )
                    }
                }
                IconButton(
                    onClick = {
                        navController.navigate(TripAppScheme.CreateTravel.name)
                        currentScreen = TripAppScheme.CreateTravel
                    },
                    modifier = Modifier.weight(1f)
                )
                {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Bottom
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.plus_icon),
                            contentDescription = "travel",
                            tint = MaterialTheme.colorScheme.onPrimaryContainer,
                            modifier = Modifier
                                .size(35.dp)
                                .clip(RoundedCornerShape(8.dp))

                        )
                        Divider(
                            modifier = Modifier
                                .height(8.dp)
                                .width(25.dp)
                                .clip(RoundedCornerShape(8.dp)),
                            color = if (currentScreen == TripAppScheme.CreateTravel) MaterialTheme.colorScheme.onPrimaryContainer else Color(
                                0x00FFFFFF
                            )
                        )
                    }
                }

                IconButton(onClick = {
                    navController.navigate(TripAppScheme.Profile.name)
                    currentScreen = TripAppScheme.Profile
                }, modifier = Modifier.weight(1f)) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Bottom
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.profile_icon),
                            contentDescription = "profile",
                            tint = MaterialTheme.colorScheme.onPrimaryContainer,
                            modifier = Modifier
                                .size(35.dp)
                                .clip(RoundedCornerShape(8.dp))
                        )
                        Divider(
                            modifier = Modifier
                                .height(8.dp)
                                .width(25.dp)
                                .clip(RoundedCornerShape(8.dp)),
                            color = if (currentScreen == TripAppScheme.Profile) MaterialTheme.colorScheme.onPrimaryContainer else Color(
                                0x00FFFFFF
                            )
                        )
                    }
                }

            }
        }*/
}


@Composable
fun ArrowBack(
    navigateUp: () -> Unit,
) {
    IconButton(onClick = navigateUp) {
        Icon(
            imageVector = Icons.Filled.Close,
            contentDescription = "back"
        )
    }
}


@Composable
fun TripPairApp(
    navController: NavHostController = rememberNavController(),
    mainController: NavController,
) {
    val profileViewModel: ProfileViewModel = viewModel()
    val homeViewModel: HomeViewModel = viewModel()
    val createViewModel: CreateViewModel = viewModel()
    var travel: TravelModel = TravelModel()
    Scaffold(
        bottomBar = { BottomBarApp(navController = navController) },
        modifier = Modifier
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = TripAppScheme.WorldMap.name,
            enterTransition = { fadeIn(animationSpec = tween(0)) },
            exitTransition = { fadeOut(animationSpec = tween(0)) },
            modifier = Modifier
                .padding(innerPadding)

                .fillMaxSize()
        ) {
            composable(TripAppScheme.WorldMap.name) {
                WorldMapStart(
                    onclick = { travelT ->
                        travel = travelT
                        navController.popBackStack()
                        navController.navigate(TripAppScheme.TravelDetail.name)
                    },
                    viewModel = homeViewModel
                )
            }
            composable(TripAppScheme.Profile.name) {
                Profile(
                    onClick = {
                        navController.popBackStack()
                        navController.navigate(
                            TripAppScheme.Diary.name
                        )
                    },
                    mainController = mainController,
                    userViewModel = profileViewModel
                )
            }
            composable(TripAppScheme.CreateTravel.name) { CreateTravel(createViewModel = createViewModel) }
            composable(TripAppScheme.TravelDetail.name) {
                TravelDetail(
                    travel = travel,
                ) {
                    navController.popBackStack()
                    navController.navigate(TripAppScheme.WorldMap.name)
                }
            }
            composable(TripAppScheme.Diary.name) { TravelDiary() }


        }

    }
}

@Preview(showBackground = true)
@Composable
fun TripPairAppPreview() {
    TripPairApp(mainController = rememberNavController())
}