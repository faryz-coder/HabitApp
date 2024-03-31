package com.bmh.habitapp.screen.detail.screen.score

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBackIosNew
import androidx.compose.material.icons.rounded.Menu
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.bmh.habitapp.R
import com.bmh.habitapp.screen.Screen
import com.bmh.habitapp.screen.detail.DetailViewModel
import com.bmh.habitapp.screen.detail.bottomNav
import com.bmh.habitapp.utils.toRankDescription
import kotlinx.coroutines.launch

@Composable
fun ScoreScreen(
    navHostController: NavHostController,
    detailViewModel: DetailViewModel
) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    CompositionLocalProvider(
        LocalLayoutDirection provides LayoutDirection.Rtl
    ) {
        ModalNavigationDrawer(
            scrimColor = Color.Transparent,
            drawerState = drawerState,
            drawerContent = {
                ModalDrawerSheet(
                    modifier = Modifier.fillMaxWidth(0.6f)
                ) {
                    getScoreList()
                }
            },
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(20.dp),
            ) {
                CompositionLocalProvider(
                    LocalLayoutDirection provides LayoutDirection.Ltr
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
                        IconButton(onClick = {
                            scope.launch {
                                drawerState.open()
                            }
                        }) {
                            Icon(Icons.Rounded.Menu, contentDescription = "Menu")
                        }
                    }
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(50.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text("Your current achievement:")
                        Box(
                            modifier = Modifier
                                .size(200.dp)
                                .border(width = 2.dp, color = Color.Black, shape = CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                modifier = Modifier.fillMaxWidth(),
                                text = detailViewModel.loginInfo.rank,
                                textAlign = TextAlign.Center,
                                fontSize = 50.sp
                            )
                        }
                        Text(
                            detailViewModel.loginInfo.rank.toRankDescription()
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun getScoreList() {
    val listArray = LocalContext.current.resources.getStringArray(R.array.score_overview)
    val score = mutableListOf<Score>()

    listArray.map {
        val i = it.split(",")
        score.add(Score(i[0], i[1], i[2].toInt()))
    }
    CompositionLocalProvider(
        LocalLayoutDirection provides LayoutDirection.Ltr
    ) {

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(text = "List of achievements")
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(
                items = score,
            ) { item ->
                Row(
                    horizontalArrangement = Arrangement.spacedBy(20.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(50.dp)
                            .border(width = 2.dp, color = Color.Black, shape = CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            modifier = Modifier.fillMaxWidth(),
                            text = item.score,
                            textAlign = TextAlign.Center,
                            fontSize = 20.sp
                        )
                    }
                    Text(item.description)
                }
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun ScoreScreenPreview() {
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
                ScoreScreen(rememberNavController(), DetailViewModel())
            }
            bottomNav(navController = rememberNavController())
        }
    }
}