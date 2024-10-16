package com.example.trippair.ui.home.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.trippair.R

import com.example.trippair.models.TravelModel
import com.example.trippair.models.User
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Locale


//     SISTEMARE LE IMMAGINI

@Composable
fun TravelCard(
    travel: TravelModel,
    onclick: (TravelModel) -> Unit,
) {

    val currencyFormatter = NumberFormat.getCurrencyInstance()
    currencyFormatter.minimumFractionDigits = 0
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clickable { onclick(travel) },
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            contentColor = MaterialTheme.colorScheme.onPrimaryContainer
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 6.dp
        ),

        ) {


        Row(
            modifier = Modifier
                .padding(16.dp)
                .height(180.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {

            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
            ) {

                HeaderTravelCard(
                    travel = travel
                )
                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = travel.title,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Start,
                )
                Spacer(modifier = Modifier.height(8.dp))

                if (travel.description.length > 150) {
                    Text(
                        text = travel.description.substring(0, 150) + "...",
                        fontSize = 13.sp,
                        lineHeight = 16.sp,
                    )
                } else {
                    Text(
                        text = travel.description,
                        fontSize = 13.sp,
                        lineHeight = 16.sp,
                    )
                }
            }
            Column(
                modifier = Modifier.fillMaxHeight(),
                verticalArrangement = Arrangement.SpaceBetween,
                horizontalAlignment = Alignment.End
            ) {
                Text(
                    text = travel.stateDestination.toUpperCase(Locale.ITALY),
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,

                    )

                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = "BUDGET\nPREVISTO",
                        fontSize = 10.sp,
                        lineHeight = 12.sp,
                        textAlign = TextAlign.End
                    )

                    Text(
                        text = currencyFormatter.format(travel.budget),
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp,
                        color = MaterialTheme.colorScheme.primary,
                    )
                }
            }

        }
    }
}


@Composable
fun ImageProfile(
    user: User,
    modifier: Modifier = Modifier.size(40.dp),
) {
    Image(
        painter = painterResource(id = R.drawable.null_account),//painterResource(id = user.profileImageUrl),
        contentDescription = user.id,
        modifier = modifier.clip(RoundedCornerShape(15.dp)),
        contentScale = ContentScale.Crop
    )
}


@Composable
fun HeaderTravelCard(
    travel: TravelModel,
) {
    val formatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    val fStartDate = formatter.format(travel.startDate)
    val fEndDate = formatter.format(travel.endDate)

    Row(verticalAlignment = Alignment.CenterVertically) {
        ImageProfile(user = travel.author)
        Column {
            Text(
                text = travel.author.name.toUpperCase(Locale.ITALY) + " " + travel.author.surname.toUpperCase(
                    Locale.ITALY
                ),
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                modifier = Modifier.padding(start = 7.dp)
            )

            Text(
                text = "DAL $fStartDate AL $fEndDate",
                fontSize = 12.sp,
                modifier = Modifier.padding(start = 7.dp)

            )
        }
    }
}


