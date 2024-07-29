package com.soundhub.ui.pages.notifications.components

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.net.toUri
import androidx.navigation.NavHostController
import com.soundhub.R
import com.soundhub.Route
import com.soundhub.data.model.Invite
import com.soundhub.data.model.User
import com.soundhub.ui.shared.avatar.CircularAvatar
import com.soundhub.ui.pages.notifications.NotificationViewModel

@Composable
internal fun FriendRequestNotification(
    invite: Invite,
    notificationViewModel: NotificationViewModel,
    navController: NavHostController
) {
    val sender: User = invite.sender
    val senderFullName: String = sender.getFullName()

    LaunchedEffect(key1 = invite) {
        Log.d("FriendRequestNotificationItem", invite.toString())
    }

    NotificationItem(
        notification = invite,
        onClick = { onNotificationClick(invite, navController) }
    ) {
        Column {
            Row(
                horizontalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                CircularAvatar(
                    imageUri = invite.sender.avatarUrl?.toUri(),
                    modifier = Modifier.size(48.dp)
                )
                Text(
                    text = buildAnnotatedString {
                        withStyle(SpanStyle(fontWeight = FontWeight.Bold)) {
                            append(senderFullName)
                        }
                        append(" ")
                        append(stringResource(R.string.notification_screen_friend_request_content))
                    },
                    softWrap = true,
                    fontSize = 18.sp,
                )
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(space = 5.dp, alignment = Alignment.CenterHorizontally),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 10.dp)
            ) {
                Button(
                    onClick = { notificationViewModel.acceptInvite(invite) },
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                ) {
                    Text(text = stringResource(id = R.string.notification_screen_accept_invite_text))
                }

                Button(
                    onClick = { notificationViewModel.rejectInvite(invite) },
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
                ) {
                    Text(text = stringResource(id = R.string.notification_screen_reject_invite_text))
                }
            }
        }
    }
}

private fun onNotificationClick(
    invite: Invite,
    navController: NavHostController
) {
    val route: String = Route.Profile.getStringRouteWithNavArg(invite.sender.id.toString())
    navController.navigate(route)
}