package com.soundhub.ui.messenger.chat.components.message_box

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.soundhub.data.model.Message
import com.soundhub.data.model.User
import com.soundhub.ui.messenger.chat.ChatUiState
import com.soundhub.ui.messenger.chat.ChatViewModel
import com.soundhub.ui.messenger.chat.components.MessageDateChip
import com.soundhub.ui.states.UiState
import com.soundhub.ui.viewmodels.UiStateDispatcher
import java.time.LocalDate

@Composable
fun MessageBoxContainer(
    modifier: Modifier = Modifier,
    chatViewModel: ChatViewModel,
    lazyListState: LazyListState,
    uiStateDispatcher: UiStateDispatcher
) {
    val chatUiState: ChatUiState by chatViewModel.chatUiState.collectAsState()
    val messages: List<Message> = chatUiState.chat?.messages ?: emptyList()
    val uiState: UiState by uiStateDispatcher.uiState.collectAsState()

    val user: User? = uiState.authorizedUser
    var messagesGroupedByDate: Map<LocalDate, List<Message>> by rememberSaveable {
        mutableStateOf(emptyMap())
    }

    val totalMessageCount by remember {
        derivedStateOf { lazyListState.layoutInfo.totalItemsCount }
    }

    LaunchedEffect(key1 = messages) {
        messagesGroupedByDate = messages
            .groupBy { it.timestamp.toLocalDate() }
            .toSortedMap()
    }

    LaunchedEffect(key1 = totalMessageCount) {
        if (totalMessageCount > 0 && messages.last().isRead)
            lazyListState.scrollToItem(totalMessageCount - 1)
    }

    LazyColumn(
        state = lazyListState,
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        var lastDate: LocalDate? = null

        messagesGroupedByDate.forEach { (date, messages) ->
            if (lastDate != date) {
                lastDate = date
                lastDate?.let {
                    item { MessageDateChip(date = date) }
                }
            }

            items(messages.sortedBy { it.timestamp }, key = { it.id }) { message ->
                MessageBox(
                    modifier = Modifier,
                    message = message,
                    isOwnMessage = message.sender?.id == user?.id,
                    uiStateDispatcher = uiStateDispatcher
                )
            }
        }
    }
}
