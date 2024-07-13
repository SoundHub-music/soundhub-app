package com.soundhub.ui.chat.components.message_box

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.soundhub.data.model.Message
import com.soundhub.data.states.ChatUiState
import com.soundhub.ui.chat.ChatViewModel

@Composable
fun MessageBox(
    modifier: Modifier = Modifier,
    message: Message,
    isOwnMessage: Boolean,
    chatViewModel: ChatViewModel,
) {
    var contentAlignment by remember { mutableStateOf(Alignment.CenterEnd) }

    val chatUiState: ChatUiState by chatViewModel.chatUiState.collectAsState()
    val isCheckMessagesModeEnabled: Boolean = chatUiState.isCheckMessageModeEnabled
    val checkedMessages: List<Message> = chatUiState.checkedMessages

    LaunchedEffect(key1 = isCheckMessagesModeEnabled) {
        Log.d("MessageBox", "isCheckedMessagesMode: $isCheckMessagesModeEnabled")
    }

    val messageParameters = object {
        val boxGradient = listOf(
            Color(0xFFD0BCFF),
            Color(0xFF966BF1)
        )
        val contentColor = if (isOwnMessage)
            MaterialTheme.colorScheme.onSecondaryContainer
        else
            MaterialTheme.colorScheme.background

        val boxModifier = if (isOwnMessage) modifier
            .background(
                color = MaterialTheme.colorScheme.secondaryContainer,
                shape = RoundedCornerShape(10.dp)
            )
        else modifier
            .background(
                brush = Brush.verticalGradient(boxGradient),
                shape = RoundedCornerShape(10.dp)
            )
    }

    LaunchedEffect(isOwnMessage) {
        contentAlignment = if (isOwnMessage)
            Alignment.CenterEnd
        else Alignment.CenterStart
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .pointerInput(isCheckMessagesModeEnabled, checkedMessages) {
                if (isOwnMessage) chatViewModel.onMessagePointerInputEvent(
                    scope = this,
                    checkedMessages = checkedMessages,
                    isCheckMessagesMode = isCheckMessagesModeEnabled,
                    message = message
                )
            },
        contentAlignment = contentAlignment
    ) {
        BadgedBox(badge = {
            if (isCheckMessagesModeEnabled && message in checkedMessages) {
                Badge(
                    containerColor = MaterialTheme.colorScheme.errorContainer
                ) { Icon(
                    imageVector = Icons.Rounded.Check,
                    contentDescription = "checked message",
                    modifier = Modifier
                        .clip(CircleShape)
                        .padding(5.dp)
                        .size(20.dp)
                ) }
            }
        }) {
            Box(modifier = messageParameters.boxModifier.padding(10.dp)) {
                Column(horizontalAlignment = Alignment.End) {
                    Row(
                        modifier = Modifier.width(IntrinsicSize.Max),
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                        verticalAlignment = Alignment.Bottom
                    ) {
                        Text(
                            text = message.content,
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
                        message = message,
                        contentColor = messageParameters.contentColor,
                        isOwnMessage = isOwnMessage
                    )
                }
            }
        }
    }
}
