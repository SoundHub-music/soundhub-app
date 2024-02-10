package com.soundhub.ui.messenger

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import com.soundhub.ui.components.containers.ContentContainer
import com.soundhub.ui.messenger.components.ChatItem
import com.soundhub.ui.messenger.components.ChatList

@Composable
fun MessengerScreen(
    navController: NavHostController
) {
    val chats = listOf(
        ChatItem(
            "Name",
            "LastName",
            "last message",
            userAvatarUrl = "https://t3.ftcdn.net/jpg/05/85/86/44/360_F_585864419_kgIYUcDQ0yiLOCo1aRjeu7kRxndcoitz.jpg"
        ),

        ChatItem(
            "Name",
            "LastName",
            "last message"
        ),

        ChatItem(
            "Name",
            "LastName",
            "last message"
        )
    )

    ContentContainer {
        ChatList(
            chats = chats,
            navController = navController
        )
    }
}