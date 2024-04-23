package com.soundhub.ui.messenger.chat.components.input_box

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.soundhub.ui.authentication.AuthenticationViewModel
import com.soundhub.ui.authentication.states.UserState
import com.soundhub.ui.messenger.chat.ChatUiState
import com.soundhub.ui.messenger.chat.ChatViewModel

@Composable
fun MessageInputBox(
    modifier: Modifier = Modifier,
    lazyListState: LazyListState,
    chatViewModel: ChatViewModel,
    authViewModel: AuthenticationViewModel
) {
    val messageContent = rememberSaveable { mutableStateOf("") }
    val chatUiState: ChatUiState by chatViewModel.chatUiState.collectAsState()
    val authorizedUserState: UserState by authViewModel
        .userInstance
        .collectAsState()

    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(
                color = MaterialTheme.colorScheme.surfaceContainer,
                shape = RoundedCornerShape(16.dp)
            ),
        horizontalArrangement = Arrangement.spacedBy(5.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        AttachFileButton()
        MessageTextField(
            messageContent = messageContent,
            modifier = Modifier.weight(1f)
        )
       EmojiAndSendMessageButtonRow(
           authorizedUserState = authorizedUserState,
           chatUiState = chatUiState,
           messageContent = messageContent,
           lazyListState = lazyListState,
           chatViewModel = chatViewModel
       )
    }
}