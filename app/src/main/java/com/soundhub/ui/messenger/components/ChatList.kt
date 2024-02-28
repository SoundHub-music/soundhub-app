package com.soundhub.ui.messenger.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.soundhub.UiStateDispatcher
import com.soundhub.data.model.Chat
import com.soundhub.data.model.User


@Composable
internal fun ChatList(
    modifier: Modifier = Modifier,
    chats: List<Chat> = emptyList(),
    uiStateDispatcher: UiStateDispatcher = hiltViewModel(),
    navController: NavHostController
) {
    LazyColumn(
        modifier = modifier
            .fillMaxWidth()
            .padding(top = 5.dp, bottom = 5.dp),
        verticalArrangement = Arrangement.spacedBy(5.dp)
    ) {
        items(items = chats, key = { it.id }) {
            ChatCard(chat = it, navController = navController)
        }
    }
}

@Composable
@Preview
fun ChatListPreview() {
    val navController = rememberNavController()
    val authorizedUser = User(
        firstName = "Test",
        lastName = "user"
    )

    val chats = listOf(
        Chat(
            lastMessage = "last message",
            participants = listOf(
                User(
                    firstName = "Alexey",
                    lastName = "Zaycev"
                ),
                authorizedUser
            )
        ),

        Chat(
            lastMessage = "last message looooooooooooooooooooooooooooooooooooooooooooooooooooooooooong",
            participants = listOf(
                User(
                    firstName = "Nikolay",
                    lastName = "Shein"
                ),
                authorizedUser
            )
        ),

        Chat(
            lastMessage = "last message",
            unreadMessageCount = 2,
            participants = listOf(
                User(
                    firstName = "Gennadiy",
                    lastName = "Bukin"
                ),
                authorizedUser
            )
        )
    )

    ChatList(chats = chats, navController = navController)
}