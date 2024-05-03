package com.soundhub.ui.messenger.chat

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.paint
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.soundhub.R
import com.soundhub.ui.components.bars.top.ChatTopAppBar
import com.soundhub.ui.components.containers.ContentContainer
import com.soundhub.ui.messenger.chat.components.MessageDateChip
import com.soundhub.ui.messenger.chat.components.message_box.MessageBoxContainer
import com.soundhub.ui.messenger.chat.components.input_box.MessageInputBox
import com.soundhub.ui.viewmodels.UiStateDispatcher
import java.util.UUID

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun ChatScreen(
    chatId: UUID,
    chatViewModel: ChatViewModel = hiltViewModel(),
    uiStateDispatcher: UiStateDispatcher,
    navController: NavHostController
) {
    val backgroundImage: Painter = painterResource(id = R.drawable.chat_background)
    val chatUiState: ChatUiState by chatViewModel.chatUiState.collectAsState()
    val lazyListState = rememberLazyListState()
    val itemIndex by remember {
        derivedStateOf { lazyListState.firstVisibleItemIndex }
    }

    LaunchedEffect(key1 = true) {
        chatViewModel.loadChatById(chatId)
    }

    LaunchedEffect(key1 = chatUiState) {
        Log.d("ChatScreen", chatUiState.toString())
    }

    if (chatUiState.chat?.messages?.isNotEmpty() == true) {
        val message = chatUiState.chat?.messages!![itemIndex]
        MessageDateChip(date = message.timestamp.toLocalDate())
    }

    Scaffold(
        topBar = {
            ChatTopAppBar(
                navController = navController,
                chatViewModel = chatViewModel
            )
        }
    ) {
        ContentContainer(
            modifier = Modifier
                .paint(painter = backgroundImage, contentScale = ContentScale.Crop)
                .background(Color.Transparent)
                .padding(horizontal = 5.dp, vertical = 10.dp),
        ) {
            Column(verticalArrangement = Arrangement.SpaceBetween) {
                MessageBoxContainer(
                    messages = chatUiState.chat?.messages ?: emptyList(),
                    lazyListState = lazyListState,
                    uiStateDispatcher = uiStateDispatcher,
                    modifier = Modifier.weight(1f)
                )
                MessageInputBox(
                    lazyListState = lazyListState,
                    modifier = Modifier,
                    chatViewModel = chatViewModel,
                    uiStateDispatcher = uiStateDispatcher
                )
            }
        }
    }
}