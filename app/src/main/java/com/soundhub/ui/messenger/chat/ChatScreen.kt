package com.soundhub.ui.messenger.chat

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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import com.soundhub.R
import com.soundhub.ui.components.containers.ContentContainer
import com.soundhub.ui.messenger.chat.components.MessageBoxContainer
import com.soundhub.ui.messenger.chat.components.MessageInputBox
import com.soundhub.utils.DateFormatter
import java.time.LocalDate

@Composable
fun MessengerChatScreen(
    chatId: String? = null,
    chatViewModel: ChatViewModel = hiltViewModel()
) {
    val backgroundImage: Painter = painterResource(id = R.drawable.chat_background)
    val messages = chatViewModel.messages.collectAsState().value
//    val messages = listOf(
//        Message(content = "fgsrsg", timestamp = LocalDateTime.of(2024, Month.DECEMBER, 15, 12, 12)),
//        Message(content = "fgsrsg"),
//        Message(content = "fgsrsg"),
//        Message(content = "fgsrsg"),
//        Message(content = "fgsrsg"),
//        Message(content = "fgsrsg"),
//        Message(content = "fgsrsg"),
//        Message(content = "fgsrsg"),
//        Message(content = "fgsrsg"),
//        Message(content = "fgsrsg"),
//        Message(content = "fgsrsg"),
//    )
    val lazyListState = rememberLazyListState()
    val itemIndex by remember {
        derivedStateOf { lazyListState.firstVisibleItemIndex }
    }

    if (messages.isNotEmpty()) {
        val message = messages[itemIndex]
        MessageDateChip(date = message.timestamp.toLocalDate())
    }

    ContentContainer(
        modifier = Modifier
            .paint(painter = backgroundImage, contentScale = ContentScale.Crop)
            .background(Color.Transparent)
            .padding(start = 5.dp, end = 5.dp, bottom = 10.dp),
    ) {
        Column(verticalArrangement = Arrangement.SpaceBetween) {
            MessageBoxContainer(
                messages = messages,
                lazyListState = lazyListState,
                modifier = Modifier.weight(1f)
            )
            MessageInputBox(
                lazyListState = lazyListState,
                modifier = Modifier,
                chatViewModel = chatViewModel
            )

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
                text = DateFormatter.getStringMonthAndDay(date),
            )
        }
    }
}