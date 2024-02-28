package com.soundhub.ui.messenger.chat.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.Send
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.soundhub.R
import com.soundhub.data.model.Message
import com.soundhub.ui.authentication.AuthenticationViewModel
import com.soundhub.ui.messenger.chat.ChatViewModel
import kotlinx.coroutines.launch

@Composable
fun MessageInputBox(
    modifier: Modifier = Modifier,
    lazyListState: LazyListState,
    chatViewModel: ChatViewModel = hiltViewModel(),
    authViewModel: AuthenticationViewModel = hiltViewModel()
) {
    var messageContent by remember { mutableStateOf("") }
    val scope = rememberCoroutineScope()
    val messages = chatViewModel.messages.collectAsState().value
    val authorizedUser = authViewModel.userInstance.collectAsState().value

    Row(
        modifier = modifier
            .background(
                color = MaterialTheme.colorScheme.surfaceContainer,
                shape = RoundedCornerShape(16.dp)
            )
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(5.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = { /* TODO: implement logic for file attaching */ }) {
            Icon(painter = painterResource(id = R.drawable.baseline_attach_file_24), contentDescription = null)
        }

        OutlinedTextField(
            modifier = Modifier
                .height(72.dp)
                .fillMaxWidth()
                .weight(1f),
            singleLine = true,
            value = messageContent,
            onValueChange = { messageContent = it },
            colors = TextFieldDefaults.colors(
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                focusedContainerColor = MaterialTheme.colorScheme.surfaceContainer,
                unfocusedContainerColor = MaterialTheme.colorScheme.surfaceContainer,
                unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
                focusedTextColor = MaterialTheme.colorScheme.onSurface
            ),
            placeholder = {
                Column(
                    modifier = Modifier
                        .fillMaxSize(),
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = stringResource(id = R.string.message_input_placeholder),
                        modifier = Modifier
                    )
                }
            }
        )

        IconButton(onClick = {
            /*TODO: implement logic for sending messages */
            chatViewModel.sendMessage(Message(
                content = messageContent,
                sender = authorizedUser
            ))

            messageContent = ""
            scope.launch {
                lazyListState.scrollToItem(messages.size)
            }
        }) {
            Icon(
                imageVector = Icons.AutoMirrored.Rounded.Send,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary
            )
        }
    }
}

@Composable
@Preview
fun ChatInputBlockPreview() {
    MessageInputBox(lazyListState = rememberLazyListState())
}