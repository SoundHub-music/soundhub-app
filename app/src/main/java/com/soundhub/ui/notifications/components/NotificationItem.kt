package com.soundhub.ui.notifications.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedIconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.soundhub.data.model.Invite
import com.soundhub.data.model.Notification
import com.soundhub.ui.components.avatar.CircularAvatar
import com.soundhub.ui.notifications.NotificationViewModel
import com.soundhub.utils.DateFormatter

@Composable
fun NotificationItem(
    modifier: Modifier = Modifier,
    notification: Notification,
    onClick: () -> Unit = {},
    content: @Composable () -> Unit
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .shadow(
                elevation = 5.dp,
                ambientColor = MaterialTheme.colorScheme.onSurface,
                shape = RoundedCornerShape(10.dp)
            )
            .background(
                color = MaterialTheme.colorScheme.secondaryContainer,
                shape = RoundedCornerShape(10.dp)
            ),
        onClick = { onClick() }
    ) {
        Row(
            modifier = Modifier.padding(15.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            content()
        }
    }
}

@Composable
internal fun FriendRequestNotificationItem(
    invite: Invite,
    notificationViewModel: NotificationViewModel
) {
    NotificationItem(
        notification = invite,
        onClick = {}
    ) {
        CircularAvatar(
            imageUrl = invite.sender.avatarUrl,
            modifier = Modifier.size(48.dp)
        )

        Column {
            Text(
                text = buildAnnotatedString {
                    withStyle(
                        SpanStyle(fontWeight = FontWeight.Bold)
                    ) {
                        append("${invite.sender.firstName} ${invite.sender.lastName}")
                    }
                    append(" ")
                    append("отправил вам заявку в друзья")
                },
                softWrap = true,
                fontSize = 18.sp,
            )

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = DateFormatter.toFullStringDate(invite.createdDateTime),
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSecondaryContainer
                )

                Row {
                    OutlinedIconButton(
                        onClick = { notificationViewModel.acceptInvite(invite) },
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.Check,
                            contentDescription = "accept invite button",
                            tint = Color.Green
                        )
                    }

                    OutlinedIconButton(
                        onClick = { notificationViewModel.rejectInvite(invite.id) },
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.Close,
                            contentDescription = "reject invite button",
                            tint = Color.Red
                        )
                    }
                }
            }
        }
    }
}