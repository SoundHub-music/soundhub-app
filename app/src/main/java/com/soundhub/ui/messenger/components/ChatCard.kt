package com.soundhub.ui.messenger.components

import android.content.Context
import android.net.Uri
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.net.toUri
import com.soundhub.R
import com.soundhub.data.model.Chat
import com.soundhub.data.model.Message
import com.soundhub.data.model.User
import com.soundhub.ui.components.avatar.CircularAvatar
import com.soundhub.ui.messenger.MessengerViewModel
import com.soundhub.data.states.UiState
import com.soundhub.ui.viewmodels.UiStateDispatcher

@Composable
internal fun ChatCard(
    chat: Chat,
    uiStateDispatcher: UiStateDispatcher,
    messengerViewModel: MessengerViewModel
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.inverseOnSurface),
        shape = RoundedCornerShape(12.dp),
        onClick = { messengerViewModel.onChatCardClick(chat) },
        modifier = Modifier
            .fillMaxWidth()
            .padding(5.dp)
            .shadow(5.dp, RoundedCornerShape(12.dp))
    ) {
        ChatCardContainer(
            messengerViewModel = messengerViewModel,
            uiStateDispatcher = uiStateDispatcher,
            chat = chat
        )
    }
}

@Composable
private fun ChatCardContainer(
    messengerViewModel: MessengerViewModel,
    uiStateDispatcher: UiStateDispatcher,
    chat: Chat
) {
    val context: Context = LocalContext.current
    val uiState: UiState by uiStateDispatcher.uiState.collectAsState(initial = UiState())
    val authorizedUser: User? = uiState.authorizedUser

    var interlocutor: User? by remember { mutableStateOf(null) }

    val messages: List<Message> = chat.messages.sortedBy { it.timestamp }
    var lastMessageState by rememberSaveable { mutableStateOf("") }
    val unreadMessageCount = remember { mutableIntStateOf(0) }

    LaunchedEffect(key1 = authorizedUser, key2 = chat) {
        authorizedUser?.let {
            interlocutor = chat.participants.find {
                it.id != authorizedUser.id
            }

            unreadMessageCount.intValue = messages.count {
                !it.isRead && it.sender?.id != authorizedUser.id
            }
        }

    }

    LaunchedEffect(messages) {
        lastMessageState = if (messages.isNotEmpty()) {
            val prefix = context.getString(R.string.messenger_screen_last_message_prefix)
            messengerViewModel.prepareMessagePreview(prefix, messages.last())
        } else ""
    }

    Row(
        modifier = Modifier.padding(16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        ChatCardAvatar(
            messengerViewModel = messengerViewModel,
            unreadMessageCount = unreadMessageCount.asIntState(),
            interlocutor = interlocutor,
            chat = chat
        )

        ChatCardDetails(
            messengerViewModel = messengerViewModel,
            unreadMessageCount = unreadMessageCount.asIntState(),
            interlocutor = interlocutor,
            chat = chat
        )
    }
}

@Composable
private fun ChatCardAvatar(
    messengerViewModel: MessengerViewModel,
    unreadMessageCount: State<Int>,
    chat: Chat,
    interlocutor: User?,
) {
    val interlocutorAvatarUrl: Uri? = remember(interlocutor) {
        interlocutor?.avatarUrl?.toUri()
    }

    BadgedBox(
        badge = {
            if (unreadMessageCount.value > 0) {
                Badge(modifier = Modifier.offset(x = (-35).dp)) {
                    Text(text = messengerViewModel.getUnreadMessageCountByChatId(chat.id).toString())
                }
            }
        }
    ) {
        CircularAvatar(
            imageUri = interlocutorAvatarUrl,
            modifier = Modifier.size(40.dp)
        )
    }
}

@Composable
private fun ChatCardDetails(
    messengerViewModel: MessengerViewModel,
    unreadMessageCount: State<Int>,
    interlocutor: User?,
    chat: Chat
) {
    val context: Context = LocalContext.current

    val messages: List<Message> = chat.messages.sortedBy { it.timestamp }
    var lastMessageState by rememberSaveable { mutableStateOf("") }
    val interlocutorFullName: String = remember(interlocutor) {
        "${interlocutor?.firstName.orEmpty()} ${interlocutor?.lastName.orEmpty()}".trim()
    }

    val lastMessageModifier = if (unreadMessageCount.value > 0) {
        Modifier
            .background(MaterialTheme.colorScheme.primary, RoundedCornerShape(16.dp))
            .padding(vertical = 2.dp, horizontal = 6.dp)
    } else Modifier

    LaunchedEffect(messages) {
        lastMessageState = if (messages.isNotEmpty()) {
            val prefix = context.getString(R.string.messenger_screen_last_message_prefix)
            messengerViewModel.prepareMessagePreview(prefix, messages.last())
        } else ""
    }

    Column {
        Text(
            text = interlocutorFullName,
            fontWeight = FontWeight.ExtraBold,
            fontSize = 16.sp,
            lineHeight = 24.sp
        )

        Text(
            text = lastMessageState,
            modifier = lastMessageModifier,
            letterSpacing = 0.25.sp,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            fontWeight = if (unreadMessageCount.value > 0) FontWeight.Bold else FontWeight.Light,
            fontSize = 14.sp,
            lineHeight = 20.sp,
            color = if (unreadMessageCount.value > 0) MaterialTheme.colorScheme.onTertiary
            else MaterialTheme.colorScheme.onPrimaryContainer
        )
    }
}