package com.soundhub.ui.messenger.components

import android.content.Context
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
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
import com.soundhub.ui.messenger.MessengerUiState
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
            .shadow(
                elevation = 5.dp,
                shape = RoundedCornerShape(12.dp),
            )
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

    val interlocutor: User? by remember {
        mutableStateOf(chat?.participants?.firstOrNull { it.id != authorizedUser?.id })
    }

    val messengerUiState: MessengerUiState by messengerViewModel.messengerUiState.collectAsState()
    val messages: List<Message> = chat?.messages.orEmpty().sortedBy { it.timestamp }

    val unreadMessageCount: Int = messengerUiState.unreadMessagesCount
    var lastMessageState by rememberSaveable { mutableStateOf("") }
    val hasUnreadMessages by remember { derivedStateOf { unreadMessageCount > 0 } }

    val lastMessageColor: Color = MaterialTheme.colorScheme.primary
    val lastMessageModifier = remember(hasUnreadMessages) {
        if (hasUnreadMessages) {
            Modifier.background(
                    color = lastMessageColor,
                    shape = RoundedCornerShape(16.dp)
                )
                .padding(vertical = 2.dp, horizontal = 6.dp)
        } else Modifier
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
                    Text(text = unreadMessageCount.toString())
                }
            }
        }
    ) {
        CircularAvatar(
            imageUrl = interlocutor?.avatarUrl?.toUri(),
            modifier = Modifier.size(40.dp)
        )
    }
    Column {
        Text(
            text = "${interlocutor?.firstName.orEmpty()} ${interlocutor?.lastName.orEmpty()}".trim(),
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
    val route: String = Route.Messenger.Chat.getStringRouteWithNavArg(chat?.id.toString())
    navController.navigate(route)
}