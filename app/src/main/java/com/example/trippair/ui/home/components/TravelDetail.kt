package com.example.trippair.ui.home.components

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.capitalize
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.trippair.ArrowBack
import com.example.trippair.R
import com.example.trippair.db.UserProfile
import com.example.trippair.models.TravelModel
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Locale

//     SISTEMARE LE IMMAGINI E I PARTECIPANTI

@Composable
fun TravelDetail(travel: TravelModel, onclick: () -> Unit) {
    val formatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    val fStartDate = formatter.format(travel.startDate)
    val fEndDate = formatter.format(travel.endDate)
    val currencyFormatter = NumberFormat.getCurrencyInstance()
    currencyFormatter.minimumFractionDigits = 0
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.onPrimary)
            .verticalScroll(ScrollState(0))
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 12.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer
            ),
            /* elevation = CardDefaults.cardElevation(
                 defaultElevation = 6.dp
             ),*/
        ) {
            Column(modifier = Modifier.padding(16.dp)) {

                HeaderTravelDetail(travel, onclick)

                Divider(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp, bottom = 16.dp)
                )
                Text(
                    text = travel.title,
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.size(16.dp))
                Text(text = "Periodo:", fontWeight = FontWeight.Bold)
                Text(
                    text = "$fStartDate - $fEndDate",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Medium

                )
                Text(
                    text = travel.description,
                    fontSize = 17.sp,
                    modifier = Modifier.padding(top = 12.dp)
                )
                if (travel.completed) {

                } else {
                    Destinations(travel = travel)
                }
                Row(verticalAlignment = Alignment.Bottom) {
                    Text(
                        text = "Budget previsto: ",
                        fontWeight = FontWeight.Bold,
                        fontSize = 17.sp,
                        modifier = Modifier.padding(top = 12.dp)
                    )
                    Text(
                        text = currencyFormatter.format(travel.budget),
                        fontWeight = FontWeight.Bold,
                        fontSize = 17.sp,
                        color = MaterialTheme.colorScheme.primary,
                    )
                }
                if (travel.transport != null) {
                    Row(verticalAlignment = Alignment.Bottom) {
                        Text(
                            text = "Mezzo di trasporto: ",
                            fontWeight = FontWeight.Bold,
                            fontSize = 17.sp,
                            modifier = Modifier.padding(top = 12.dp)
                        )
                        Text(
                            text = travel.transport,
                            fontWeight = FontWeight.Bold,
                            fontSize = 17.sp,
                            color = MaterialTheme.colorScheme.primary,
                        )
                    }
                }
                Text(
                    text = "Partecipanti: ",
                    fontWeight = FontWeight.Bold,
                    fontSize = 17.sp,
                    modifier = Modifier.padding(top = 12.dp)
                )
                if (travel.participants.isEmpty()) {
                    Text(
                        text = "Nessun partecipante ancora",
                        fontSize = 17.sp,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .padding(top = 12.dp)
                            .fillMaxWidth(),
                        color = MaterialTheme.colorScheme.primary
                    )
                }
                Row(
                    modifier = Modifier
                        .height(80.dp)
                        .horizontalScroll(rememberScrollState())
                ) {
                    travel.participants.forEach {
                        IconButton(onClick = { /*TODO*/ }, modifier = Modifier.size(80.dp)) {
                            ImageProfile(
                                user = travel.author,
                                modifier = Modifier.size(60.dp)
                            )
                        }

                    }
                }


                Button(
                    onClick = { /*TODO*/ },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                ) {
                    Text(text = "Partecipa", fontWeight = FontWeight.Bold)
                }



                Divider(modifier = Modifier.padding(top = 8.dp, bottom = 16.dp))
                Text(
                    text = "Informazioni sull'utente",
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.size(16.dp))
                Text(
                    text = travel.author.email,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center
                )
                Text(
                    text = travel.author.phone,
                    modifier = Modifier
                        .padding(top = 6.dp)
                        .fillMaxWidth(),
                    textAlign = TextAlign.Center
                )
                if (travel.author.reputation != null) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 6.dp),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {


                        Text(
                            text = travel.author.reputation.toString(),
                            modifier = Modifier,
                            textAlign = TextAlign.Center
                        )
                        Icon(
                            painter = painterResource(id = R.drawable.baseline_star_24),
                            contentDescription = "star",
                            modifier = Modifier
                        )
                    }
                }
            }

        }
    }
}

@Composable
fun HeaderTravelDetail(travel: TravelModel, onclick: () -> Unit) {
    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
        ImageProfile(
            user = travel.author,
            modifier = Modifier.size(60.dp)
        )
        Row(
            modifier = Modifier.weight(1f),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {


            Column {
                Text(text = "Creator", fontSize = 12.sp, modifier = Modifier.padding(start = 8.dp))

                Text(
                    text = "${travel.author.name.toUpperCase(locale = Locale.ITALY)} ${
                        travel.author.surname.toUpperCase(
                            locale = Locale.ITALY
                        )
                    }",
                    modifier = Modifier.padding(start = 8.dp),
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                    textDecoration = TextDecoration.Underline
                )
            }
            ArrowBack(navigateUp = onclick)
        }
    }
}

@Composable
fun Destinations(travel: TravelModel) {
    Text(
        text = "Itinerario:",
        fontWeight = FontWeight.Bold,
        fontSize = 17.sp,
        modifier = Modifier.padding(top = 12.dp)
    )
    Row(
        modifier = Modifier
            .padding(top = 12.dp)
            .fillMaxWidth()
            .horizontalScroll(state = ScrollState(0)),

        horizontalArrangement = Arrangement.Center
    ) {
        var destinations = travel.citiesDestination.size
        travel.citiesDestination.forEach {

            Text(
                text = it.capitalize(Locale.ITALY),
                color = MaterialTheme.colorScheme.onPrimary,
                modifier = Modifier
                    .background(
                        color = MaterialTheme.colorScheme.primary,
                        shape = CircleShape
                    )
                    .padding(8.dp),
                fontSize = 17.sp,
                fontWeight = FontWeight.Bold
            )
            if (destinations > 1) {
                Icon(
                    painter = painterResource(id = R.drawable.circle),
                    contentDescription = "circle",
                    modifier = Modifier
                        .padding(10.dp),
                    tint = MaterialTheme.colorScheme.primary
                )
            }
            destinations--

        }

    }
}
