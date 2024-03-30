package com.soundhub.ui.messenger.chat.components.input_box

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.soundhub.R
import com.soundhub.ui.authentication.AuthenticationViewModel
import com.soundhub.ui.authentication.states.UserState
import com.soundhub.ui.messenger.chat.ChatViewModel

@Composable
fun MessageInputBox(
    modifier: Modifier = Modifier,
    lazyListState: LazyListState,
    chatViewModel: ChatViewModel = hiltViewModel(),
    authViewModel: AuthenticationViewModel = hiltViewModel()
) {
    val messageContent = rememberSaveable { mutableStateOf("") }
    val messages by chatViewModel.messages.collectAsState()
    val authorizedUser: UserState by authViewModel
        .userInstance
        .collectAsState(initial = UserState())

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
                messageCount = messages.size,
                lazyListState = lazyListState,
                authorizedUser = authorizedUser.current,
                chatViewModel = chatViewModel
            )
        }
    }
}

@Composable
@Preview
fun ChatInputBlockPreview() {
    MessageInputBox(lazyListState = rememberLazyListState())
}