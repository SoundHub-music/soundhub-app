package com.soundhub.ui.pages.chat.components.input_box

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
import com.soundhub.data.states.ChatUiState
import com.soundhub.ui.pages.chat.ChatViewModel
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
            if (messageCount > 0)
                scope.launch { lazyListState.scrollToItem(messageCount - 1) }
        }) {
        Icon(
            imageVector = Icons.AutoMirrored.Rounded.Send,
            contentDescription = "send message button",
            tint = MaterialTheme.colorScheme.primary
        )
    }
}