package com.soundhub.ui.messenger.chat.components.message_box

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.soundhub.data.model.Message
import com.soundhub.data.model.User
import com.soundhub.ui.states.UiState
import com.soundhub.ui.viewmodels.UiStateDispatcher

@Composable
fun MessageBoxContainer(
    modifier: Modifier = Modifier,
    messages: List<Message> = emptyList(),
    uiStateDispatcher: UiStateDispatcher,
    lazyListState: LazyListState
) {
    val uiState: UiState by uiStateDispatcher.uiState.collectAsState()
    val user: User? = uiState.authorizedUser

    LazyColumn(
        state = lazyListState,
        modifier = modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        items(items = messages) { message ->
            MessageBox(
                modifier = Modifier,
                messageData = message,
                isOwnMessage = message.sender?.id == user?.id
            )
        }
    }
}