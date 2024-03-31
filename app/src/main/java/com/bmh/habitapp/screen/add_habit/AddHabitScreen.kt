package com.bmh.habitapp.screen.add_habit

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.rounded.MergeType
import androidx.compose.material3.Card
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.bmh.habitapp.manager.FirestoreManager
import com.bmh.habitapp.model.BadHabits
import com.bmh.habitapp.model.Constant.EVENT
import com.bmh.habitapp.model.Constant.TIME
import com.bmh.habitapp.utils.currentDate
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddHabitScreen(navHostController: NavHostController) {
    var badHabits by rememberSaveable { mutableStateOf("") }
    var consumption by rememberSaveable { mutableStateOf("1") }
    var cost by rememberSaveable { mutableStateOf("0") }
    var date by rememberSaveable { mutableStateOf("26/12/2023") }
    var type by rememberSaveable { mutableStateOf("Money") }

    val focusManager  = LocalFocusManager.current
    val sheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()
    var showBottomSheet by remember {
        mutableStateOf(false)
    }
    val interactionSource = remember { MutableInteractionSource() }

    Surface(
        color = MaterialTheme.colorScheme.background,
        modifier = Modifier.clickable(interactionSource = interactionSource, indication = null) {
            focusManager.clearFocus()
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(start = 30.dp, end = 30.dp, top = 20.dp, bottom = 20.dp),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = badHabits,
                    onValueChange = { badHabits = it },
                    label = { Text(text = "Bad Habit") }
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Card(
                    ) {
                        Column(
                            modifier = Modifier
                                .padding(20.dp)
                                .size(120.dp, 50.dp),
                            verticalArrangement = Arrangement.SpaceEvenly
                        ) {
                            Row(
                                verticalAlignment = Alignment.Bottom,
                                horizontalArrangement = Arrangement.spacedBy(5.dp)
                            ) {
                                Icon(
                                    Icons.Filled.CalendarToday,
                                    contentDescription = "Today Calendar Auto Generate"
                                )
                                Text(text = "Date")
                            }
                            Text(text = currentDate(), fontWeight = FontWeight.Bold)
                        }
                    }
                    ElevatedCard(
                        onClick = {
                            showBottomSheet = true
                        }
                    ) {
                        Column(
                            modifier = Modifier
                                .padding(20.dp)
                                .size(120.dp, 50.dp),
                            verticalArrangement = Arrangement.SpaceEvenly
                        ) {
                            Row(
                                verticalAlignment = Alignment.Bottom,
                                horizontalArrangement = Arrangement.spacedBy(5.dp)
                            ) {
                                Icon(
                                    Icons.Rounded.MergeType,
                                    contentDescription = "Today Calendar Auto Generate"
                                )
                                Text(text = "Select Type")
                            }
                            Text(text = "(${type.capitalize()})", fontWeight = FontWeight.Bold)
                        }
                    }
                }

                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    enabled = type != EVENT,
                    value = consumption,
                    onValueChange = {
                        if (!it.contains('.') && !it.contains(',')) {
                            consumption = it
                        }
                    },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    label = { Text(text = "Daily Consumption (Default 1)") }
                )

                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    enabled = type != EVENT,
                    value = cost,
                    onValueChange = {
                        if (!it.contains(',')) {
                            cost = it
                        }
                    },
                    maxLines = 1,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    label = {
                        Text(
                            text = if (type != TIME) {
                                "Unit Cost (RM)"
                            } else {
                                "Unit Per Hour"
                            }
                        )
                    }
                )

            }

            ElevatedButton(onClick = {
                FirestoreManager().addHabits(
                    badHabits = BadHabits(
                        habits = badHabits,
                        consumption = consumption.toLong(),
                        cost = cost.toLong(),
                        beginDate = date,
                        type = type
                    ),
                    onSuccess = {
                        navHostController.popBackStack()
                    },
                    onFailed = {
                        Toast.makeText(
                            navHostController.context,
                            "Failed Creating Habits",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                )
            }) {
                Text(text = "Start!")
            }
            if (showBottomSheet) {
                ModalBottomSheet(
                    onDismissRequest = { showBottomSheet = false },
                    sheetState = sheetState
                ) {
                    Box(
                        modifier = Modifier.padding(bottom = 20.dp)
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                        ) {
                            TextButton(
                                modifier = Modifier.fillMaxWidth(),
                                onClick = {
                                    type = "money"
                                    scope.launch { sheetState.hide() }.invokeOnCompletion {
                                        if (!sheetState.isVisible) {
                                            showBottomSheet = false
                                        }
                                    }
                                }) {
                                Text(text = "Money")
                            }
                            HorizontalDivider()
                            TextButton(
                                modifier = Modifier.fillMaxWidth(),
                                onClick = {
                                    type = "time"
                                    scope.launch { sheetState.hide() }.invokeOnCompletion {
                                        if (!sheetState.isVisible) {
                                            showBottomSheet = false
                                        }
                                    }
                                }) {
                                Text(text = "Time")
                            }
                            HorizontalDivider()
                            TextButton(
                                modifier = Modifier.fillMaxWidth(),
                                onClick = {
                                    type = "event"
                                    scope.launch { sheetState.hide() }.invokeOnCompletion {
                                        if (!sheetState.isVisible) {
                                            showBottomSheet = false
                                        }
                                    }
                                }) {
                                Text(text = "Event")
                            }
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AddHabitScreenPreview() {
    AddHabitScreen(navHostController = rememberNavController())
}