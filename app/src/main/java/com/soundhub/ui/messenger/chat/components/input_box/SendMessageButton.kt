package com.soundhub.ui.messenger.chat.components.input_box

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.Send
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import com.soundhub.ui.messenger.chat.ChatUiState
import com.soundhub.ui.messenger.chat.ChatViewModel
import kotlinx.coroutines.launch

@Composable
fun SendMessageButton(
    chatViewModel: ChatViewModel,
    messageCount: Int,
    lazyListState: LazyListState,
) {
    val scope = rememberCoroutineScope()
    val chatUiState: ChatUiState by chatViewModel.chatUiState.collectAsState()
    val messageContent: String = chatUiState.messageContent

    IconButton(
        enabled = messageContent.isNotEmpty(),
        onClick = {
            chatViewModel.sendMessage()
            scope.launch { lazyListState.scrollToItem(messageCount) }
        }) {
        Icon(
            imageVector = Icons.AutoMirrored.Rounded.Send,
            contentDescription = "send message button",
            tint = MaterialTheme.colorScheme.primary
        )
    }
}