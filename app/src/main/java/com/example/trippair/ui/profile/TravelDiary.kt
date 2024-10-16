package com.example.trippair.ui.profile

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun TravelDiary() {
    var size1 by remember { mutableStateOf(350) }
    var size2 by remember { mutableStateOf(350) }
    var scrollState by remember {
        mutableIntStateOf(0)
    }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.onPrimary)

    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, top = 16.dp, end = 16.dp)

        ) {

            Text(
                text = "DIARIO DI VIAGGIO",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .padding(bottom = 15.dp, top = 30.dp)
                    .fillMaxWidth()
            )
            Column(modifier = if (size1 == 350) Modifier.verticalScroll(ScrollState(scrollState)) else Modifier) {
                Spacer(modifier = Modifier.height(30.dp))
                Text(text = "Ultimi viaggi")
                var j = 0
                LazyColumn(modifier = Modifier.height(size1.dp)) {
                    items(4) {
                        j++
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(150.dp)
                                .padding(8.dp)
                        ) {
                            Text(
                                text = "Viaggio $j",
                                modifier = Modifier
                                    .padding(8.dp)
                                    .align(Alignment.CenterHorizontally)
                            )

                        }
                    }
                }
                IconButton(
                    onClick = {
                        size1 = if (size1 == 350) 500 else 350
                        scrollState = 0
                    },
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                ) {
                    Icon(
                        imageVector = if (size1 == 350) Icons.Filled.KeyboardArrowDown else Icons.Filled.KeyboardArrowUp,
                        contentDescription = "espandi"
                    )
                }
                var i = 0
                Text(text = "Viaggi in programma")
                LazyColumn(modifier = Modifier.height(size2.dp)) {
                    items(4) {
                        i++
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(150.dp)
                                .padding(8.dp)
                        ) {
                            Text(
                                text = "Viaggio $i",
                                modifier = Modifier
                                    .padding(8.dp)
                                    .align(Alignment.CenterHorizontally)
                            )
                        }
                    }
                }
                IconButton(
                    onClick = {
                        size2 = if (size2 == 350) 500 else 350
                        scrollState = 10000
                    },
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                ) {
                    Icon(
                        imageVector = if (size2 == 350) Icons.Filled.KeyboardArrowDown else Icons.Filled.KeyboardArrowUp,
                        contentDescription = "espandi"
                    )
                }
            }

        }
    }
}


@Preview(showBackground = true)
@Composable
fun TravelDiaryPreview() {
    TravelDiary()
}
