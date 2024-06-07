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
import androidx.navigation.NavHostController
import com.soundhub.R
import com.soundhub.Route
import com.soundhub.data.model.Chat
import com.soundhub.data.model.Message
import com.soundhub.data.model.User
import com.soundhub.ui.components.avatar.CircularAvatar
import com.soundhub.ui.messenger.MessengerViewModel
import com.soundhub.ui.states.UiState
import com.soundhub.ui.viewmodels.UiStateDispatcher

@Composable
internal fun ChatCard(
    chat: Chat?,
    navController: NavHostController,
    uiStateDispatcher: UiStateDispatcher,
    messengerViewModel: MessengerViewModel
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.inverseOnSurface),
        shape = RoundedCornerShape(12.dp),
        onClick = { onChatCardClick(chat, navController) },
        modifier = Modifier
            .fillMaxWidth()
            .padding(5.dp)
            .shadow(5.dp, RoundedCornerShape(12.dp))
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            ChatDetails(
                messengerViewModel = messengerViewModel,
                uiStateDispatcher = uiStateDispatcher,
                chat = chat
            )
        }
    }
}

@Composable
private fun ChatDetails(
    messengerViewModel: MessengerViewModel,
    uiStateDispatcher: UiStateDispatcher,
    chat: Chat?
) {
    val context: Context = LocalContext.current
    val uiState: UiState by uiStateDispatcher.uiState.collectAsState(initial = UiState())
    val authorizedUser: User? = uiState.authorizedUser

    var interlocutor: User? by remember { mutableStateOf(null) }
    val interlocutorFullName: String = remember(interlocutor) {
        "${interlocutor?.firstName.orEmpty()} ${interlocutor?.lastName.orEmpty()}".trim()
    }
    val interlocutorAvatarUrl: Uri? = remember(interlocutor) { interlocutor?.avatarUrl?.toUri() }

    val messengerUiState by messengerViewModel.messengerUiState.collectAsState()
    val unreadMessageCount = messengerUiState.unreadMessagesCount
    val messages: List<Message> = remember(chat) {
        chat?.messages.orEmpty().sortedBy { it.timestamp }
    }

    var lastMessageState by rememberSaveable { mutableStateOf("") }
    val hasUnreadMessages = unreadMessageCount > 0

    val lastMessageModifier = if (hasUnreadMessages) {
        Modifier
            .background(MaterialTheme.colorScheme.primary, RoundedCornerShape(16.dp))
            .padding(vertical = 2.dp, horizontal = 6.dp)
    } else Modifier

    LaunchedEffect(authorizedUser, chat) {
        interlocutor = chat?.participants?.find { it.id != authorizedUser?.id }
    }

    LaunchedEffect(messages) {
        lastMessageState = if (messages.isNotEmpty()) {
            val prefix = context.getString(R.string.messenger_screen_last_message_prefix)
            messengerViewModel.prepareMessagePreview(prefix, messages.last())
        } else ""
    }

    BadgedBox(
        badge = {
            if (hasUnreadMessages) {
                Badge(modifier = Modifier.offset(x = (-35).dp)) {
                    Text(text = messengerViewModel.getUnreadMessageCountByChatId(chat?.id).toString())
                }
            }
        }
    ) {
        CircularAvatar(
            imageUrl = interlocutorAvatarUrl,
            modifier = Modifier.size(40.dp)
        )
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
            fontWeight = if (hasUnreadMessages) FontWeight.Bold else FontWeight.Light,
            fontSize = 14.sp,
            lineHeight = 20.sp,
            color = if (hasUnreadMessages) MaterialTheme.colorScheme.onTertiary
            else MaterialTheme.colorScheme.onPrimaryContainer
        )
    }
}

private fun onChatCardClick(chat: Chat?, navController: NavHostController) {
    chat?.id?.let {
        val route = Route.Messenger.Chat.getStringRouteWithNavArg(it.toString())
        navController.navigate(route)
    }
}
