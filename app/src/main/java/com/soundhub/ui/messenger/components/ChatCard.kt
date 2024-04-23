package com.soundhub.ui.messenger.components

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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.soundhub.Route
import com.soundhub.data.model.Chat
import com.soundhub.data.model.User
import com.soundhub.ui.authentication.AuthenticationViewModel
import com.soundhub.ui.components.avatar.CircularAvatar
import com.soundhub.ui.messenger.chat.ChatUiState
import com.soundhub.ui.messenger.chat.ChatViewModel

@Composable
internal fun ChatCard(
    chat: Chat?,
    navController: NavHostController,
    authenticationViewModel: AuthenticationViewModel,
    chatViewModel: ChatViewModel = hiltViewModel()
) {
    var lastMessageModifier: Modifier = Modifier
    val chatUiState: ChatUiState by chatViewModel.chatUiState.collectAsState()
    val unreadMessageCount = chatUiState.unreadMessageCount
    val hasUnreadMessages: Boolean = unreadMessageCount > 0
    val authorizedUser by authenticationViewModel
        .userInstance
        .collectAsState()

    val interlocutor: User? = chat?.participants?.firstOrNull {
        it.id != authorizedUser.current?.id
    }
    val lastMessage: String = if (chat?.messages?.isNotEmpty() == true)
        chat.messages.last().content.substring(20) + "..."
    else ""

    if (hasUnreadMessages)
        lastMessageModifier = Modifier
            .background(
                color = MaterialTheme.colorScheme.primary,
                shape = RoundedCornerShape(16.dp)
            )
            .padding(vertical = 2.dp, horizontal = 6.dp)

    Card(
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.inverseOnSurface),
        shape = RoundedCornerShape(12.dp),
        onClick = {
            chatViewModel.setInterlocutor(interlocutor)
            navController.navigate(Route.Messenger.Chat(chat?.id.toString()).route)
        },
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
            BadgedBox(
                badge = {
                    if (hasUnreadMessages)
                        Badge(
                            modifier = Modifier.offset(x = (-35).dp)
                        ) { Text(text = unreadMessageCount.toString()) }
                }
            ) {
                CircularAvatar(
                    imageUrl = interlocutor?.avatarUrl,
                    modifier = Modifier.size(40.dp)
                )
            }
            Column {
                Text(
                    text = "${interlocutor?.firstName} ${interlocutor?.lastName}".trim(),
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 16.sp,
                    lineHeight = 24.sp
                )
                Text(
                    text = lastMessage,
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
    }
}