package com.example.trippair.ui.home


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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll

import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.trippair.R
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.style.TextAlign

import com.example.trippair.models.TravelModel
import com.example.trippair.ui.home.components.TravelCard


@Composable
fun WorldMapStart(
    onclick: (TravelModel) -> Unit,
    viewModel: HomeViewModel,
) {
    val homeUiState by viewModel.homeState.collectAsState()
    var destination by remember { mutableStateOf("") }
    LaunchedEffect(true) {
        viewModel.initHomeState()
    }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                MaterialTheme.colorScheme.onPrimary
            )
    ) {
        Column(
            modifier = Modifier
                .padding(start = 16.dp, top = 16.dp, end = 16.dp)
        ) {
            Text(
                text = "CERCA IL TUO PROSSIMO VIAGGIO",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .padding(20.dp)
                    .align(Alignment.CenterHorizontally)
            )

            SearchField(destination, homeViewModel = viewModel) { destination = it }

            Column(
                modifier = Modifier
                    .padding(top = 16.dp)
                    .verticalScroll(rememberScrollState())
            ) {

                Spacer(modifier = Modifier.height(25.dp))
                Text(text = if (destination.isEmpty()) "Viaggi in evidenza" else "Ricerche per $destination")
                Spacer(modifier = Modifier.height(10.dp))
                for (travel in homeUiState.travels) {
                    TravelCard(travel = travel, onclick = onclick)

                }


            }
        }
    }
}

@Composable
fun SearchField(
    destination: String,
    homeViewModel: HomeViewModel,
    onValueChange: (String) -> Unit,
) {

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        OutlinedTextField(
            value = destination,
            onValueChange = onValueChange,
            placeholder = {
                Text(
                    "Inserisci destinazione",
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
            },
            modifier = Modifier
                .clip(RoundedCornerShape(16.dp))
                .background(MaterialTheme.colorScheme.primaryContainer)
                .height(50.dp)
                .weight(2f),

            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color.Transparent,
                unfocusedBorderColor = Color.Transparent,
            ),
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = androidx.compose.ui.text.input.ImeAction.Done
            ),

            )
        Spacer(modifier = Modifier.width(4.dp))
        IconButton(
            onClick = { homeViewModel.searchTravel(destination) }, modifier = Modifier
                .size(48.dp)
                .weight(0.3f)
                .background(
                    MaterialTheme.colorScheme.primaryContainer,
                    shape = RoundedCornerShape(16.dp)
                )


        ) {

            Icon(
                painter = painterResource(id = R.drawable.home_icon),
                contentDescription = "Search",
                tint = MaterialTheme.colorScheme.onPrimaryContainer,
                modifier = Modifier.size(40.dp)
            )
        }
    }
}



