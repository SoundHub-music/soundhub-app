package com.soundhub.ui.messenger.chat.components.input_box

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import com.soundhub.R
import com.soundhub.ui.authentication.states.UserState
import com.soundhub.ui.messenger.chat.ChatUiState
import com.soundhub.ui.messenger.chat.ChatViewModel

@Composable
internal fun EmojiAndSendMessageButtonRow(
    authorizedUserState: UserState,
    chatUiState: ChatUiState,
    messageContent: MutableState<String>,
    lazyListState: LazyListState,
    chatViewModel: ChatViewModel
) {
    Row {
        IconButton(onClick = { /*TODO: implement emoji panel */ }) {
            Icon(
                painter = painterResource(id = R.drawable.baseline_emoji_emotions_24),
                contentDescription = "emoji",
                tint = colorResource(id = R.color.emoji_btn_dark)
            )
        }
        SendMessageButton(
            messageContent = messageContent,
            messageCount = chatUiState.chat?.messages?.size ?: 0,
            lazyListState = lazyListState,
            authorizedUser = authorizedUserState.current,
            chatViewModel = chatViewModel
        )
    }
}