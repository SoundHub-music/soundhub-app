package com.soundhub.ui.messenger.chat.components.message_box

import android.view.View
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.unit.dp
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.soundhub.data.model.Message
import com.soundhub.data.model.User
import com.soundhub.ui.messenger.chat.ChatUiState
import com.soundhub.ui.messenger.chat.ChatViewModel
import com.soundhub.ui.messenger.chat.components.MessageDateChip
import com.soundhub.ui.states.UiState
import com.soundhub.ui.viewmodels.UiStateDispatcher
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate
import java.util.UUID

@Composable
fun MessageBoxContainer(
    modifier: Modifier = Modifier,
    chatViewModel: ChatViewModel,
    lazyListState: LazyListState,
    uiStateDispatcher: UiStateDispatcher
) {
    val chatUiState: ChatUiState by chatViewModel.chatUiState.collectAsState()
    val messages: List<Message> = remember(chatUiState.chat?.messages) {
        chatUiState.chat?.messages.orEmpty().sortedBy { it.timestamp }
    }

    val uiState: UiState by uiStateDispatcher.uiState.collectAsState(initial = UiState())
    val readMessageChannel: Flow<Message> = uiStateDispatcher.readMessages
    val deletedMessageChannel: Flow<UUID> = uiStateDispatcher.deletedMessages

    val authorizedUser: User? = uiState.authorizedUser

    var messagesGroupedByDate: Map<LocalDate, List<Message>> by remember {
        mutableStateOf(emptyMap())
    }

    val totalMessageCount by remember {
        derivedStateOf { lazyListState.layoutInfo.totalItemsCount }
    }

    var isKeyboardActive by remember { mutableStateOf(false) }
    val view: View = LocalView.current

    LaunchedEffect(messages) {
        messagesGroupedByDate = messages
            .groupBy { it.timestamp.toLocalDate() }
            .toSortedMap()
    }


    // we listen to event when interlocutor reads our message
    LaunchedEffect(key1 = readMessageChannel) {
        readMessageChannel.collect { msg ->
            messagesGroupedByDate = messagesGroupedByDate.mapValues { entry ->
                entry.value.map { if (it.id == msg.id) msg else it }
            }
        }
    }

    // we listen to event when interlocutor deletes his own message
    LaunchedEffect(key1 = deletedMessageChannel) {
        deletedMessageChannel.collect { msgId ->
            messagesGroupedByDate = messagesGroupedByDate.mapValues { entry ->
                entry.value.filter { it.id != msgId }
            }.filter { it.value.isNotEmpty() }
        }
    }

    // when keyboard state is changing it scrolls to the last message
    LaunchedEffect(totalMessageCount, isKeyboardActive) {
        chatViewModel.scrollToLastMessage(totalMessageCount, lazyListState)
    }

    // it sets the keyboard state that allows to navigate to the end of the message list when keyboard was hidden
    DisposableEffect(view) {
        ViewCompat.setOnApplyWindowInsetsListener(view) { _, insets ->
            val isVisible = insets.isVisible(WindowInsetsCompat.Type.ime())
            isKeyboardActive = isVisible
            insets
        }

        onDispose {
            ViewCompat.setOnApplyWindowInsetsListener(view, null)
        }
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
                item { MessageDateChip(date = date) }
            }

            items(messages, key = { it.id }) { message ->
                MessageBox(
                    modifier = Modifier,
                    message = message,
                    isOwnMessage = message.sender?.id == authorizedUser?.id,
                    chatViewModel = chatViewModel,
                )
            }
        }
    }
}