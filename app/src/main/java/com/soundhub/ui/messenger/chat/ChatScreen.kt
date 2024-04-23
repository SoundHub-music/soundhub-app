package com.soundhub.ui.messenger.chat

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.paint
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.soundhub.R
import com.soundhub.ui.authentication.AuthenticationViewModel
import com.soundhub.ui.components.bars.top.ChatTopAppBar
import com.soundhub.ui.components.containers.ContentContainer
import com.soundhub.ui.messenger.chat.components.MessageBoxContainer
import com.soundhub.ui.messenger.chat.components.input_box.MessageInputBox
import com.soundhub.ui.viewmodels.UiStateDispatcher
import com.soundhub.utils.DateFormatter
import java.time.LocalDate
import java.util.UUID

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun ChatScreen(
    chatId: UUID,
    chatViewModel: ChatViewModel = hiltViewModel(),
    authViewModel: AuthenticationViewModel,
    navController: NavHostController,
    uiStateDispatcher: UiStateDispatcher
) {
    val backgroundImage: Painter = painterResource(id = R.drawable.chat_background)
    val chatUiState: ChatUiState by chatViewModel.chatUiState.collectAsState()
    val lazyListState = rememberLazyListState()
    val itemIndex by remember {
        derivedStateOf { lazyListState.firstVisibleItemIndex }
    }

    LaunchedEffect(key1 = true) {
        chatViewModel.loadChat(chatId)
    }

    LaunchedEffect(key1 = chatUiState.chat?.messages) {
        Log.d("ChatScreen", chatUiState.chat?.messages.toString())
    }

    if (chatUiState.chat?.messages?.isNotEmpty() == true) {
        val message = chatUiState.chat?.messages!![itemIndex]
        MessageDateChip(date = message.timestamp.toLocalDate())
    }

    Scaffold(
        topBar = {
            ChatTopAppBar(
                navController = navController,
                chatViewModel = chatViewModel,
                uiStateDispatcher = uiStateDispatcher
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
                    modifier = Modifier.weight(1f),
                    authenticationViewModel = authViewModel
                )
                MessageInputBox(
                    lazyListState = lazyListState,
                    modifier = Modifier,
                    chatViewModel = chatViewModel,
                    authViewModel = authViewModel
                )
            }
        }
    }
}

@Composable
private fun MessageDateChip(date: LocalDate) {
    Box(
        modifier = Modifier
            .padding(top = 5.dp)
            .fillMaxWidth()
            .zIndex(1F),
        contentAlignment = Alignment.TopCenter
    ) {
        Row(
            modifier = Modifier
                .background(
                    color = MaterialTheme.colorScheme.tertiaryContainer,
                    shape = RoundedCornerShape(5.dp)
                ),
        ) {
            Text(
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp),
                text = DateFormatter.getStringDate(date),
            )
        }
    }
}