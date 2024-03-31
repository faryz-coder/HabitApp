package com.bmh.habitapp.screen.detail.screen.item_details

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBackIosNew
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.bmh.habitapp.manager.FirestoreManager
import com.bmh.habitapp.model.BadHabits
import com.bmh.habitapp.screen.detail.DetailViewModel
import com.bmh.habitapp.screen.detail.bottomNav
import com.bmh.habitapp.screen.home.HabitsItem
import com.bmh.habitapp.utils.timeDifference

@Composable
fun ItemDetailsScreen(
    navHostController: NavHostController,
    detailViewModel: DetailViewModel
) {
    val interactionSource = remember { MutableInteractionSource() }
    val focusManager = LocalFocusManager.current
    val context = LocalContext.current
    Column(
        modifier = Modifier
            .fillMaxHeight()
            .clickable(interactionSource = interactionSource, indication = null) {
                focusManager.clearFocus()
            },
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 10.dp),
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = {
                    navHostController.popBackStack()
                }) {
                    Icon(Icons.Rounded.ArrowBackIosNew, contentDescription = "Menu")
                }
                Text(text = detailViewModel.badHabits.habits, fontSize = 20.sp)
            }
        }
        Column(
            modifier = Modifier.weight(1f)
                .padding(horizontal = 20.dp)
                .padding(top = 20.dp)
                .clickable(interactionSource = interactionSource, indication = null) {
                    focusManager.clearFocus()
                }
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(20.dp),
        ) {
            HabitsItem(badHabits = detailViewModel.badHabits, detailViewModel = detailViewModel)
            HorizontalDivider()
            DateInfo(detailViewModel.badHabits)
            HorizontalDivider()
            FutureSavings(detailViewModel.badHabits)
            HorizontalDivider()
            TotalNotUsedComponent(detailViewModel.badHabits)
            HorizontalDivider()
            AddNotesComponent(context, detailViewModel.badHabits)
        }
    }
}

@Composable
fun AddNotesComponent(
    context: Context,
    badHabits: BadHabits = BadHabits()
) {
    var notes by rememberSaveable {
        mutableStateOf(badHabits.notes)
    }
    Box(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            Arrangement.spacedBy(10.dp)
        ) {
            Text(text = "Add Notes")
            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp),
                maxLines = 5,
                value = notes,
                onValueChange = { notes = it },
                label = { Text(text = "Notes") },
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                Button(onClick = {
                    if (notes.isNotEmpty()) {
                        badHabits.notes = notes
                        FirestoreManager().updateNotes(badHabits) {
                            Toast.makeText(context, "Updated", Toast.LENGTH_SHORT).show()
                        }
                    }
                }) {
                    Text(text = "Apply")
                }
            }
        }
    }
}

@Composable
fun TotalNotUsedComponent(badHabits: BadHabits) {
    Box(
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = "Total Not Used")
            Text(text = badHabits.moneyEarned.toString())
        }
    }
}

@Composable
fun FutureSavings(badHabits: BadHabits) {
    Box(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            Arrangement.spacedBy(10.dp),
            Alignment.CenterHorizontally
        ) {
            Text(text = "Future Saving")
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = "Daily")
                Text(text = badHabits.daily.toString())
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = "Weekly")
                Text(text = (badHabits.daily * 7).toString())
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = "Monthly")
                Text(text = (badHabits.daily * 29).toString())
            }
        }
    }
}

@Composable
fun DateInfo(badHabits: BadHabits) {
    Box(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = "Begin Date")
                Text(text = badHabits.beginDate)
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = "Time Passed")
                Text(text = timeDifference(badHabits.beginDate) + " Days")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ItemDetailScreenPreview() {
    Surface(
        modifier = Modifier
            .fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier.fillMaxHeight(),

            ) {
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(20.dp),
            ) {
                ItemDetailsScreen(
                    rememberNavController(),
                    detailViewModel = DetailViewModel()
                )
            }
            bottomNav(navController = rememberNavController())
        }
    }
}