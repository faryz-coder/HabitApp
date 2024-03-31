package com.bmh.habitapp.screen.detail.screen.rewards

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.ArrowBackIosNew
import androidx.compose.material.icons.rounded.DeleteForever
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.bmh.habitapp.manager.FirestoreManager
import com.bmh.habitapp.model.Rewards
import com.bmh.habitapp.screen.Screen
import com.bmh.habitapp.screen.detail.DetailViewModel
import com.bmh.habitapp.screen.detail.bottomNav

@Composable
fun RewardScreen(navHostController: NavHostController, detailViewModel: DetailViewModel) {
    var badHabits = remember {
        mutableStateOf(detailViewModel.badHabits)
    }

    try {
        FirestoreManager().getMoneyAvailableAndEarned(detailViewModel) {
            badHabits.value = it
            detailViewModel.badHabits = it
        }
    } catch (e: Exception) {
    }


    Column(
        verticalArrangement = Arrangement.spacedBy(20.dp),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 10.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = {
                    navHostController.popBackStack(Screen.ItemDetail.route, false)
                }) {
                    Icon(Icons.Rounded.ArrowBackIosNew, contentDescription = "Menu")
                }
                Text(text = detailViewModel.badHabits.habits, fontSize = 20.sp)
            }
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Text(text = "Available:")
                Text(text = "RM ${badHabits.value.moneyEarned - badHabits.value.spend}")
            }
            Row(
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Text(text = "Total spent:")
                Text(text = "RM ${badHabits.value.spend}")
            }
        }
        RewardList(detailViewModel)
    }
}

@Preview(showBackground = true)
@Composable
fun RewardList(detailViewModel: DetailViewModel? = null) {
    val list: MutableList<Rewards> = remember { mutableStateListOf() }

    try {
        FirestoreManager().getReward(
            badHabits = detailViewModel!!.badHabits,
            onSuccess = {
                if (list != it) {
                    list.clear()
                    list.addAll(it)
                }
            },
            onFailed = {}
        )
    } catch (e: Exception) {
    }


    LazyVerticalGrid(
        modifier = Modifier.padding(horizontal = 20.dp),
        columns = GridCells.Fixed(2),
        horizontalArrangement = Arrangement.spacedBy(20.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        itemsIndexed(
            items = list
        ) { index, item ->
            if (index == 0) {
                if (detailViewModel != null) {
                    RewardContent(
                        detailViewModel,
                        list
                    )
                }
            } else {
                if (detailViewModel != null) {
                    RewardItem(
                        detailViewModel, rewards = item, list = list
                    )
                }
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RewardContent(detailViewModel: DetailViewModel, list: MutableList<Rewards>) {
    val sheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()
    var showBottomSheet by remember {
        mutableStateOf(false)
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp),
        onClick = {
            showBottomSheet = true
        }
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(10.dp),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                modifier = Modifier.fillMaxSize(),
                imageVector = Icons.Rounded.Add, contentDescription = "Add Rewards"
            )
        }
    }

    if (showBottomSheet) {
        ModalBottomSheet(
            onDismissRequest = {
                showBottomSheet = false
            },
            sheetState = sheetState
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(30.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                ItemContent(detailViewModel, list) { showBottomSheet = false }
            }
        }
    }
}

@Composable
@Preview(showBackground = true)
fun ItemContent(
    detailViewModel: DetailViewModel? = null,
    list: MutableList<Rewards>? = null,
    closeSheet: () -> Unit = {}
) {
    var rewardName by remember {
        mutableStateOf("")
    }
    var rewardPrice by remember {
        mutableStateOf("")
    }
    Column(
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            label = {
                Text(text = "Reward's name")
            },
            value = rewardName, onValueChange = { rewardName = it }
        )
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            label = {
                Text(text = "Reward's price")
            },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            value = rewardPrice, onValueChange = { rewardPrice = it }
        )
        Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = {
                list?.add(
                    Rewards(
                        "", rewardName, rewardPrice.toFloat(), 1
                    )
                )

                detailViewModel?.badHabits?.let {
                    FirestoreManager().addReward(
                        it,
                        Rewards(
                            name = rewardName,
                            price = rewardPrice.toFloat()
                        ),
                        onSuccess = { closeSheet.invoke() },
                        onFailed = { closeSheet.invoke() }
                    )
                }
            }) {
            Text("Save")
        }
    }
}

fun isEligableToBuy(detailViewModel: DetailViewModel, rewards: Rewards): Boolean {
    val available = detailViewModel.badHabits.moneyEarned - detailViewModel.badHabits.spend
    return (available - rewards.price.toLong()) >= 0
}

@Composable
fun RewardItem(detailViewModel: DetailViewModel, rewards: Rewards, list: MutableList<Rewards>) {
    val openAlertDialog = remember { mutableStateOf(false) }
    val buyItemAlertDialog = remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp),
        onClick = {
            if (isEligableToBuy(detailViewModel, rewards)) {
                buyItemAlertDialog.value = true
            }
        }
    ) {
        Box {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(10.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = rewards.name)
                Text(text = "RM ${rewards.price}")
                Text(text = "${rewards.owned} Purchased")
            }
            IconButton(
                modifier = Modifier
                    .align(Alignment.TopEnd),
                onClick = { openAlertDialog.value = true }) {
                Icon(Icons.Rounded.DeleteForever, contentDescription = null)
            }
        }
    }

    if (openAlertDialog.value) {
        AlertDialog(
            onDismissRequest = {
                openAlertDialog.value = false
            },
            confirmButton = {
                TextButton(onClick = {
                    list.remove(rewards)
                    FirestoreManager().deleteReward(detailViewModel.badHabits, rewards, {}, {})
                    openAlertDialog.value = false
                }) {
                    Text(text = "Confirm")
                }
            },
            dismissButton = {
                TextButton(onClick = {
                    openAlertDialog.value = false
                }) {
                    Text(text = "Dismiss")
                }
            },
            title = {
                Text(text = "Confirm deletion?")
            },
            text = {
                Text(text = "All the money spend on this reward will be returned")
            },
        )
    }

    if (buyItemAlertDialog.value) {
        BuyConfirmationDialog(name = rewards.name,
            onConfirm = {
                FirestoreManager().buyItem(detailViewModel, rewards).let {
                    buyItemAlertDialog.value = false
                }
            },
            onDismiss = { buyItemAlertDialog.value = false }
        )
    }
}

@Composable
@Preview(showBackground = true)
fun BuyConfirmationDialog(
    name: String = "",
    onConfirm: () -> Unit = {},
    onDismiss: () -> Unit = {}
) {
    AlertDialog(
        onDismissRequest = { onDismiss.invoke() },
        confirmButton = {
            TextButton(onClick = { onConfirm.invoke() }) {
                Text(text = "Yes")
            }
        },
        dismissButton = {
            TextButton(onClick = { onDismiss.invoke() }) {
                Text(text = "No")
            }
        }, title = { Text(text = "Confirm purchase?") },
        text = { Text(text = "Do you want to purchase $name?") }
    )
}


@Preview(showBackground = true)
@Composable
fun RewardScreenPreview() {
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
                RewardScreen(rememberNavController(), DetailViewModel())
            }
            bottomNav(navController = rememberNavController())
        }
    }
}