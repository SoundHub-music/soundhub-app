package com.soundhub.ui.messenger_chat

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.paint
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import com.soundhub.R
import com.soundhub.ui.components.containers.ContentContainer

@Composable
fun MessengerChatScreen(chatId: String? = null) {
    val backgroundImage: Painter = painterResource(id = R.drawable.chat_background)

    Box(
        modifier = Modifier
            .paint(painter = backgroundImage, contentScale = ContentScale.Crop)

    ) {
        ContentContainer(
            modifier = Modifier.background(Color.Transparent)
                .fillMaxSize()
        ) {
            Text(
                text = "id: $chatId",
                    color = Color.White
            )
        }
    }
}