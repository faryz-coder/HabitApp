package com.bmh.habitapp.startup

import android.content.Intent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.bmh.habitapp.AuthActivity
import com.bmh.habitapp.R
import com.bmh.habitapp.manager.SharePreferencesManager

@Composable
fun StartupScreen(
    navHostController: NavHostController,
    sharedPref: SharePreferencesManager? = null,
) {
    var sliderPosition by remember {
        mutableFloatStateOf(1f)
    }
    val intent = Intent(navHostController.context, AuthActivity::class.java)
    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK

    Surface(
        color = MaterialTheme.colorScheme.background
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                // CONTENT
                Box(
                    modifier = Modifier.fillMaxHeight(0.9f),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        when (sliderPosition) {
                            1f -> Content1()
                            2f -> Content2()
                            3f -> Content3()
                            4f -> Content4()
                            5f -> Content5()
                        }
                    }
                }

                // NAVIGATION
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    TextButton(onClick = {
                        sharedPref?.setNotFirstTime()
                        navHostController.context.startActivity(intent)
                    }) {
                        Text(text = "SKIP")
                    }
                    Box(
                        modifier = Modifier.fillMaxWidth(0.3f)
                    ) {
                        Slider(
                            value = sliderPosition,
                            onValueChange = {},
                            valueRange = 1f..5f,
                            steps = 3
                        )
                    }
                    TextButton(onClick = {
                        if (sliderPosition != 5f) sliderPosition =
                            (sliderPosition + 1f) else {
                            sharedPref?.setNotFirstTime()
                            navHostController.context.startActivity(
                                intent
                            )
                        }
                    }) {
                        Text(text = "NEXT")
                    }
                }
            }
        }
    }
}

@Composable
fun SetIcon(id: Int, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier.fillMaxWidth(0.6f),
        contentAlignment = Alignment.Center
    ) {
        Image(painter = painterResource(id = id), contentDescription = "")
    }
}

@Composable
fun Content1() {
    // Title
    Text(
        text = "TITLE 1",
        fontSize = 30.sp
    )
    // Icon
    SetIcon(R.drawable.habitiq_logo)
    // Content
    Text(
        text = "Content"
    )
}

@Composable
fun Content2() {
    // Title
    Text(
        text = "TITLE 2",
        fontSize = 30.sp
    )
    // Icon
    SetIcon(R.drawable.habitiq_logo)
    // Content
    Text(
        text = "Content"
    )
}

@Composable
fun Content3() {
    // Title
    Text(
        text = "TITLE 3",
        fontSize = 30.sp
    )
    // Icon
    SetIcon(R.drawable.habitiq_logo)
    // Content
    Text(
        text = "Content"
    )
}

@Composable
fun Content4() {
    // Title
    Text(
        text = "TITLE 4",
        fontSize = 30.sp
    )
    // Icon
    SetIcon(R.drawable.habitiq_logo)
    // Content
    Text(
        text = "Content"
    )
}

@Composable
fun Content5() {
    // Title
    Text(
        text = "TITLE 5",
        fontSize = 30.sp
    )
    // Icon
    SetIcon(R.drawable.habitiq_logo)
    // Content
    Text(
        text = "Content"
    )
}

@Preview(showBackground = true)
@Composable
fun PreviewStartupScreen() {
    StartupScreen(navHostController = rememberNavController())
}