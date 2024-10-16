package com.example.trippair.ui.createTravel


import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.text.format.DateUtils

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField

import androidx.compose.material3.Text
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment

import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.room.Room
import com.example.trippair.db.UserProfileDB

import java.text.NumberFormat
import java.util.Date

import java.util.Locale


//    DA SISTEMARE I CAMPI IN MODO CHE NON CRASHCINO ( IN PARTICOLARE BUGET E DATA )
//    DA IMPLEMENTARE IL TASTO SUCCESSIVO NELLA TASTIERA

@Composable
fun CreateTravel(
    createViewModel: CreateViewModel = viewModel(),
) {
    val currencyFormatter = NumberFormat.getCurrencyInstance()
    currencyFormatter.minimumFractionDigits = 0

    val createUiState by createViewModel.createState.collectAsState()

    val dbRoom = Room.databaseBuilder(
        LocalContext.current,
        UserProfileDB::class.java, "user"
    ).build()
    val userDao = dbRoom.userDao()
    val sharedPreferences: SharedPreferences =
        LocalContext.current.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
    val userId = sharedPreferences.getString("user_id", "")

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                MaterialTheme.colorScheme.onPrimary
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, top = 16.dp, end = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "CREA IL TUO VIAGGIO",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .padding(bottom = 30.dp, top = 30.dp)
                    .fillMaxWidth()
            )

            Column(
                modifier = Modifier
                    .padding(top = 20.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                TextInputField(
                    stateVariable = createUiState.title,
                    onValueChange = { createViewModel.onTitleChange(it) },
                    placeHolder = "Inserisci il titolo",
                    label = "Titolo".uppercase(Locale.ITALY),
                )

                TextInputField(
                    label = "stato",
                    placeHolder = "Inserisci lo stato di destinazione",
                    stateVariable = createUiState.stateDestination,
                    onValueChange = { createViewModel.onStateDestinationChange(it) })
                TextInputField(
                    label = "itinerario",
                    placeHolder = "Inserisci le città che visiterai",
                    stateVariable = createUiState.citiesDestination.joinToString(","),
                    onValueChange = { createViewModel.onCitiesDestinationChange(it.split(",")) })
                TextInputField(
                    label = "descrizione",
                    placeHolder = "Inserisci la descrizione",
                    minLines = 5,
                    height = 150,
                    stateVariable = createUiState.description,
                    onValueChange = { createViewModel.onDescriptionChange(it) })


                PeriodPicker("Scegli data di inizio") { date ->
                    createViewModel.onStartDateChange(
                        date
                    )
                }
                PeriodPicker("  Scegli data di fine ") { date ->
                    createViewModel.onEndDateChange(
                        date
                    )
                }
                Spacer(modifier = Modifier.height(30.dp))

                Row(
                    verticalAlignment = Alignment.Bottom,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.weight(1f)) {

                        Text(
                            text = "BUDGET PREVISTO",
                            color = MaterialTheme.colorScheme.primary,

                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(bottom = 5.dp, start = 8.dp)
                        )
                        Row(verticalAlignment = Alignment.CenterVertically) {


                            OutlinedTextField(
                                value = if (createUiState.budget == 0.0) "" else createUiState.budget.toString(),
                                onValueChange = {
                                    if (it == "") createViewModel.onBudgetChange(0.0)
                                    else createViewModel.onBudgetChange(it.toDouble())
                                },
                                placeholder = {
                                    Text(
                                        text = ""
                                    )
                                },
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                modifier = Modifier
                                    .height(50.dp)
                                    .weight(1f)
                            )

                            Text(
                                text = "€",
                                fontSize = 20.sp,
                                modifier = Modifier
                                    .weight(0.2f)
                                    .padding(start = 2.dp)
                            )
                        }
                        Spacer(modifier = Modifier.height(30.dp))
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column(modifier = Modifier.weight(1.5f)) {

                        Text(
                            text = "MEZZO DI \nTRASPORTO",
                            color = MaterialTheme.colorScheme.primary,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(bottom = 5.dp, start = 8.dp)
                        )

                        OutlinedTextField(
                            value = createUiState.transport,
                            onValueChange = { createViewModel.onTransportChange(it) },
                            placeholder = { Text(text = "Es. Aereo, bus...") },

                            modifier = Modifier
                                .height(50.dp)
                        )

                        Spacer(modifier = Modifier.height(30.dp))
                    }

                }
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Button(
                        onClick = {
                            createViewModel.onCreateTravel(userDao = userDao, userId = userId ?: "")
                            createViewModel.freeTravel()
                        },
                        modifier = Modifier.width(200.dp)
                    ) {
                        Text(text = "create")
                    }
                    Spacer(modifier = Modifier.height(30.dp))
                }

            }
        }
    }
}

@Composable
fun TextInputField(
    label: String,
    stateVariable: String,
    placeHolder: String = "",
    minLines: Int = 1,
    height: Int = 50,
    short: Boolean = false,
    onValueChange: (String) -> Unit,
) {
    Column {


        Text(
            text = label.uppercase(Locale.ITALY),
            color = MaterialTheme.colorScheme.primary,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 5.dp, start = 8.dp)
        )
        if (short)
            OutlinedTextField(
                value = stateVariable,
                onValueChange = { onValueChange(it) },
                placeholder = { Text(text = placeHolder) },
                minLines = minLines,
                modifier = Modifier
                    .height(height.dp)
                    .weight(1f)

            )
        else {
            OutlinedTextField(
                value = stateVariable,
                onValueChange = { onValueChange(it) },
                placeholder = { Text(text = placeHolder) },
                minLines = minLines,
                modifier = Modifier
                    .height(height.dp)
                    .fillMaxWidth(),
            )
            Spacer(modifier = Modifier.height(30.dp))
        }
    }
}

@SuppressLint("NewApi")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PeriodPicker(string: String, onDateChange: (Date?) -> Unit = {}) {

    val dateState = rememberDatePickerState()


    val c = dateState.selectedDateMillis?.let {
        DateUtils.formatDateTime(
            null,
            it,
            DateUtils.FORMAT_SHOW_YEAR
        )
    }
    onDateChange(dateState.selectedDateMillis?.let { Date(it) })
    var showDialog by remember { mutableStateOf(false) }
    Row(
        verticalAlignment = Alignment.CenterVertically, modifier = Modifier
            .fillMaxWidth()
            .height(50.dp)
    ) {
        Button(
            onClick = { showDialog = true },
        ) { Text(text = string) }
        Card(
            modifier = Modifier
                .fillMaxHeight()
                .padding(5.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                contentColor = MaterialTheme.colorScheme.onPrimaryContainer
            )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(3.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                if (c != null)
                    Text(text = c)
                else
                    Text(text = "Seleziona data")
            }
        }
    }
    if (showDialog) {
        DatePickerDialog(
            onDismissRequest = { showDialog = false },
            confirmButton = {
                Button(
                    onClick = { showDialog = false }
                ) {
                    Text(text = "OK")
                }
            },
            dismissButton = {
                Button(
                    onClick = { showDialog = false }
                ) {
                    Text(text = "Cancel")
                }
            }
        ) {
            DatePicker(
                state = dateState,
                showModeToggle = true
            )
        }
    }


}

