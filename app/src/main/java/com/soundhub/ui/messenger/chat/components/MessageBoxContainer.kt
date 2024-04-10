package com.soundhub.ui.messenger.chat.components

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
import com.soundhub.ui.authentication.AuthenticationViewModel
import com.soundhub.ui.authentication.states.UserState

@Composable
fun MessageBoxContainer(
    modifier: Modifier = Modifier,
    messages: List<Message> = emptyList(),
    authenticationViewModel: AuthenticationViewModel,
    lazyListState: LazyListState
) {
    val user: UserState by authenticationViewModel
        .userInstance.collectAsState(initial = UserState())

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
                isOwnMessage = message.sender?.id == user.current?.id
            )
        }
    }
}