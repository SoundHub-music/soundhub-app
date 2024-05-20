package com.soundhub.ui.messenger.chat.components.message_box

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
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
import androidx.compose.ui.input.pointer.PointerInputScope
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.soundhub.data.model.Message
import com.soundhub.data.model.User
import com.soundhub.ui.viewmodels.UiStateDispatcher

@Composable
fun MessageBox(
    modifier: Modifier = Modifier,
    message: Message,
    isOwnMessage: Boolean,
    uiStateDispatcher: UiStateDispatcher
) {
    var contentAlignment by remember { mutableStateOf(Alignment.CenterEnd) }

    val uiState by uiStateDispatcher.uiState.collectAsState()
    val isCheckMessagesMode = uiState.isCheckMessagesMode
    val checkedMessages = uiState.checkedMessages

    LaunchedEffect(key1 = isCheckMessagesMode) {
        Log.d("MessageBox", "isCheckedMessagesMode: $isCheckMessagesMode")
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
            .pointerInput(isCheckMessagesMode, checkedMessages) {
                onMessagePointerInputEvent(
                    scope = this,
                    uiStateDispatcher = uiStateDispatcher,
                    checkedMessages = checkedMessages,
                    isCheckMessagesMode = isCheckMessagesMode,
                    message = message
                )
            },
        contentAlignment = contentAlignment
    ) {
        BadgedBox(badge = {
            if (isCheckMessagesMode && message in checkedMessages) {
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

private suspend fun onMessagePointerInputEvent(
    scope: PointerInputScope,
    uiStateDispatcher: UiStateDispatcher,
    message: Message,
    isCheckMessagesMode: Boolean,
    checkedMessages: List<Message>
) {
    scope.detectTapGestures(
        onLongPress = {
            uiStateDispatcher.setCheckMessagesMode(true)
            uiStateDispatcher.addCheckedMessage(message)
        },
        onTap = {
            if (isCheckMessagesMode) {
                if (message in checkedMessages)
                    uiStateDispatcher.uncheckMessage(message)
                else uiStateDispatcher.addCheckedMessage(message)
            }
        }
    )
}

@Composable
@Preview(showBackground = true)
private fun MessageBoxPreview() {
    Column(
        verticalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        MessageBox(
            message = Message(
                content = "Message content",
                sender = User()
            ),
            isOwnMessage = true,
            uiStateDispatcher = UiStateDispatcher()
        )

        MessageBox(
            message = Message(
                content = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur",
                sender = User()
            ),
            isOwnMessage = false,
            uiStateDispatcher = UiStateDispatcher()
        )
    }
}
