package com.soundhub.ui.messenger.components

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.soundhub.UiStateDispatcher
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
    uiStateDispatcher: UiStateDispatcher = hiltViewModel(),
    navController: NavHostController
) {
    val searchBarText = uiStateDispatcher.uiState.collectAsState().value.searchBarText
    var sortedChats by rememberSaveable { mutableStateOf(chats) }

    LaunchedEffect(key1 = searchBarText) {
        Log.d("search_text", searchBarText)
        sortedChats = if (searchBarText.isNotEmpty()) {
            chats.filter {
                searchBarText.lowercase() in it.firstName.lowercase() ||
                searchBarText.lowercase() in it.lastName.lowercase()
            }
        } else chats

        Log.d("chats", sortedChats.toString())
    }

    LazyColumn(
        modifier = modifier
            .fillMaxWidth()
            .padding(top = 5.dp, bottom = 5.dp),
        verticalArrangement = Arrangement.spacedBy(5.dp)
    ) {
        items(items = sortedChats, key = { it.id }) {
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