package com.bmh.habitapp.screen.chat

import android.widget.Toast
import androidx.compose.foundation.border
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBackIosNew
import androidx.compose.material.icons.rounded.Send
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.bmh.habitapp.manager.FirestoreManager
import com.bmh.habitapp.model.Chats
import com.bmh.habitapp.screen.detail.DetailViewModel
import kotlinx.coroutines.launch

@Composable
fun ChatScreen(navHostController: NavHostController, detailViewModel: DetailViewModel) {

    val focusManager = LocalFocusManager.current
    val interactionSource = remember { MutableInteractionSource() }
    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()

    val chatHistory = remember {
        mutableStateListOf<Chats>()
    }
    val context = LocalContext.current
    FirestoreManager().getChat(
        onSuccess = {
            if (it.size != 0 && !chatHistory.containsAll(it)) {
                chatHistory.clear()
                chatHistory.addAll(it)
            }
        },
        onFailed = {
            Toast.makeText(context, "Failed to Load Chat", Toast.LENGTH_SHORT).show()
        }
    )

//    for (i in 0..2) {
//        chatHistory.add(
//            Chats(
//                "A", "darkryz@gmail.com", "Lorem Ipsum, Lorem LoremLorem Ipsum, Lorem Lorem", ""
//            )
//        )
//        chatHistory.add(
//            Chats(
//                "S", "test@gmail.com", "Lorem Ipsum, Lorem Lorem", ""
//            )
//        )
//    }

    Surface(
        modifier = Modifier
            .fillMaxSize()
            .clickable(
                interactionSource = interactionSource,
                indication = null
            ) {
                focusManager.clearFocus()
            },
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize(),
        ) {
            CustomAppBar("Chat") {
                navHostController.popBackStack()
            }

            // CHAT LIST
            Column(
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                LazyColumn(
                    state = listState,
                    modifier = Modifier
                        .padding(20.dp)
                        .weight(1f),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(
                        items = chatHistory
                    ) { item ->
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            ProfilePicture(item.rank)
                            ChatBox(item.email, item.message)
                        }
                    }
                    if (chatHistory.size > 0) {
                        coroutineScope.launch {
                            listState.animateScrollToItem(index = chatHistory.size - 1)
                        }
                    }
                }

                // CHAT INPUT
                Box(
                    modifier = Modifier.padding(start = 20.dp, end = 20.dp, bottom = 20.dp)
                ) {
                    ChatInput(detailViewModel)
                }
            }
        }
    }

}

@Composable
@Preview(showBackground = true)
fun CustomAppBar(
    title: String = "",
    onClick: () -> Unit = {}
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = onClick) {
            Icon(Icons.Rounded.ArrowBackIosNew, contentDescription = "Menu")
        }
        Text(text = title, fontSize = 20.sp)
    }
}

@Composable
@Preview(showBackground = true)
fun ChatInput(detailViewModel: DetailViewModel? = null) {
    val focusManager = LocalFocusManager.current

    var msg by remember {
        mutableStateOf("")
    }
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        OutlinedTextField(
            modifier = Modifier.weight(1f),
            value = msg, onValueChange = { msg = it })
        IconButton(
            modifier = Modifier
                .weight(0.2f)
                .padding(start = 10.dp),
            onClick = {
                if (detailViewModel != null && msg.isNotEmpty()) {
                    focusManager.clearFocus()
                    FirestoreManager().sendChat(detailViewModel, msg)
                    msg = ""
                }
            }) {
            Icon(Icons.Rounded.Send, contentDescription = "Send Message")
        }
    }
}

@Composable
@Preview(showBackground = true)
fun ChatBox(email: String = "test@emal.com", message: String = "Lorem Ipsum, Lorem Lorem") {
    OutlinedCard {
        Column(
            modifier = Modifier.padding(10.dp),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Text(text = email)
            Text(text = message)
        }
    }
}

@Composable
@Preview(showBackground = true)
fun ProfilePicture(score: String = "S") {
    Box(
        modifier = Modifier
            .size(25.dp)
            .border(width = 1.dp, color = Color.Black, shape = CircleShape),
        contentAlignment = Alignment.Center
    ) {
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = score,
            textAlign = TextAlign.Center,
            fontSize = 10.sp
        )
    }
}

@Composable
@Preview(showBackground = true)
fun ChatScreenPreview() {
    ChatScreen(navHostController = rememberNavController(), DetailViewModel())
}