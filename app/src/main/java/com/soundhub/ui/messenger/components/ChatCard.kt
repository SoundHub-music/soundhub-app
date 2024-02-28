package com.soundhub.ui.messenger.components

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.soundhub.Route
import com.soundhub.data.model.Chat
import com.soundhub.ui.authentication.AuthenticationViewModel
import com.soundhub.ui.components.CircularAvatar

@Composable
internal fun ChatCard(
    chat: Chat?,
    navController: NavHostController,
    authenticationViewModel: AuthenticationViewModel = hiltViewModel()
) {
    var lastMessageModifier: Modifier = Modifier
    val hasUnreadMessages: Boolean = (chat?.unreadMessageCount ?: 0) > 0
    val authorizedUser = authenticationViewModel.userInstance.collectAsState().value
    val interlocutor = chat?.participants?.first { it?.id != authorizedUser?.id }

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
        modifier = Modifier
            .fillMaxWidth()
            .padding(5.dp)
            .shadow(
                elevation = 5.dp,
                shape = RoundedCornerShape(12.dp),
            )
            .clickable {
                Log.d("chat", Route.Messenger.Chat(chat?.id.toString()).route)
                navController.navigate(Route.Messenger.Chat(chat?.id.toString()).route)
            }
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            BadgedBox(
                badge = {
                    if (hasUnreadMessages)
                        Badge(
                            modifier = Modifier.offset(x = (-35).dp)
                        ) { Text(text = chat?.unreadMessageCount.toString()) }
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
                    text = chat?.lastMessage ?: "",
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

@Composable
@Preview(name = "ChatCard", showBackground = true)
fun ChatCardPreview() {
    val navController = rememberNavController()
    ChatCard(chat = null, navController = navController)
}