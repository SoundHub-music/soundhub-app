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
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import java.util.UUID

data class ChatItem(
    val firstName: String,
    val lastName: String,
    val lastMessage: String = "",
    val userAvatarUrl: String? = null,
    val id: UUID = UUID.randomUUID()
)

@Composable
internal fun ChatList(
    modifier: Modifier = Modifier,
    chats: List<ChatItem> = emptyList(),
    navController: NavHostController
) {
    LazyColumn(
        modifier = modifier
            .fillMaxWidth()
            .padding(top = 5.dp, bottom = 5.dp),
        verticalArrangement = Arrangement.spacedBy(5.dp)
    ) {
        items(items = chats, key = { it.id }) {
            ChatCard(chatItem = it, navController = navController)
        }
    }
}

@Composable
@Preview
fun ChatListPreview() {
    val navController = rememberNavController()

    val chats = listOf(
        ChatItem(
            "Name",
            "LastName",
            "last message"
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

    ChatList(chats = chats, navController = navController)
}