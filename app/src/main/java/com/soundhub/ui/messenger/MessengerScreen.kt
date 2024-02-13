package com.soundhub.ui.messenger

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.soundhub.UiStateDispatcher
import com.soundhub.ui.components.containers.ContentContainer
import com.soundhub.ui.messenger.components.ChatItem
import com.soundhub.ui.messenger.components.ChatList

@Composable
fun MessengerScreen(
    navController: NavHostController,
    uiStateDispatcher: UiStateDispatcher = hiltViewModel()
) {
    val chats = listOf(
        ChatItem(
            "Alexey",
            "Zaycev",
            "last message",
            userAvatarUrl = "https://t3.ftcdn.net/jpg/05/85/86/44/360_F_585864419_kgIYUcDQ0yiLOCo1aRjeu7kRxndcoitz.jpg"
        ),

        ChatItem(
            "Nikolay",
            "Shein",
            "last message"
        ),

        ChatItem(
            "Gennadiy",
            "Bukin",
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