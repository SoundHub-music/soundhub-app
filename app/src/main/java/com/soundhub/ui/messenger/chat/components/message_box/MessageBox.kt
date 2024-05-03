package com.soundhub.ui.messenger.chat.components.message_box

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.soundhub.data.model.Chat
import com.soundhub.data.model.Message
import com.soundhub.data.model.User

@Composable
fun MessageBox(
    modifier: Modifier = Modifier,
    messageData: Message,
    isOwnMessage: Boolean
) {
    val messageParameters = object {
        val boxGradient = listOf(
            Color(0xFFD0BCFF),
            Color(0xFF966BF1)
        )

        val contentColor = if (!isOwnMessage) MaterialTheme
            .colorScheme.onBackground
        else MaterialTheme.colorScheme.background

        var boxModifier = if (isOwnMessage) modifier
            .background(
                color = Color(0xFF333333),
                shape = RoundedCornerShape(10.dp)
            )
        else modifier
            .background(
                brush = Brush.verticalGradient(boxGradient),
                shape = RoundedCornerShape(10.dp)
            )
    }

    Box(
        modifier = Modifier.fillMaxWidth(),
        contentAlignment = if (isOwnMessage) Alignment.CenterEnd
        else Alignment.CenterStart
    ) {
        Box(modifier = messageParameters.boxModifier.padding(10.dp)) {
            Column(horizontalAlignment = Alignment.End) {
                Row(
                    modifier = Modifier
                        .width(IntrinsicSize.Max),
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalAlignment = Alignment.Bottom
                ) {
                    Text(
                        text = messageData.content,
                        textAlign = TextAlign.Justify,
                        fontSize = 18.sp,
                        letterSpacing = 0.5.sp,
                        lineHeight = 24.sp,
                        fontWeight = FontWeight.Normal,
                        color = messageParameters.contentColor,
                        modifier = Modifier
                    )
                }
                MessageTimeAndMarkerRow(
                    message = messageData,
                    contentColor = messageParameters.contentColor,
                    isOwnMessage = isOwnMessage
                )
            }
        }
    }
}

@Composable
@Preview(showBackground = true)
private fun MessageBoxPreview() {
    Column(
        verticalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        MessageBox(
            messageData = Message(
                content = "Message content",
                chat = Chat(createdBy = User()),
                sender = User()
            ),
            isOwnMessage = true
        )

        MessageBox(
            messageData = Message(
                content = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur",
                chat = Chat(createdBy = User()),
                sender = User()
            ),
            isOwnMessage = false
        )
    }
}