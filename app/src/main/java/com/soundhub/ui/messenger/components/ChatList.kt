package com.soundhub.ui.messenger.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.soundhub.data.model.Chat
import com.soundhub.ui.viewmodels.UiStateDispatcher

@Composable
internal fun ChatList(
    modifier: Modifier = Modifier,
    chats: List<Chat> = emptyList(),
    navController: NavHostController,
    uiStateDispatcher: UiStateDispatcher
) {
    LazyColumn(
        modifier = modifier
            .fillMaxWidth()
            .padding(top = 5.dp, bottom = 5.dp),
        verticalArrangement = Arrangement.spacedBy(5.dp)
    ) {
        items(items = chats, key = { it.id }) { chat ->
            ChatCard(
                chat = chat,
                navController = navController,
                uiStateDispatcher = uiStateDispatcher
            )
        }
    }
}