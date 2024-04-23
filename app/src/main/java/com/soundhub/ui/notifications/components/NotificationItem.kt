package com.soundhub.ui.notifications.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.soundhub.ui.components.avatar.CircularAvatar
import com.soundhub.utils.DateFormatter
import java.time.LocalDateTime
import java.util.UUID

@Composable
fun NotificationItem(
    modifier: Modifier = Modifier,
    dateTime: LocalDateTime = LocalDateTime.now(),
    avatarUrl: String?,
    objectName: String,
    type: NotificationType
) {
    Box(
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
            )
            .clickable {
                /* TODO: implement click notification logic */
            },
    ) {
        Row(
            modifier = Modifier.padding(15.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            CircularAvatar(
                imageUrl = avatarUrl,
                modifier = Modifier.size(48.dp)
            )

            Column {
                Text(
                    text = buildAnnotatedString {
                        withStyle(
                            SpanStyle(fontWeight = FontWeight.Bold)
                        ) {
                            append(objectName)
                        }
                        append(" ")
                        when (type) {
                            NotificationType.FRIEND_REQUEST -> append("отправил вам заявку в друзья")
                            NotificationType.NEW_POST -> append("опубликовал новый пост")
                            else -> {}
                        }
                    },
                    softWrap = true,
                    fontSize = 18.sp,
                )

                Text(
                    text = DateFormatter.toFullStringDate(dateTime),
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSecondaryContainer
                )
            }
        }
    }
}


data class Notification(
    val id: UUID = UUID.randomUUID(),
    val type: NotificationType,
    val objectName: String = "",
    val dateTime: LocalDateTime = LocalDateTime.now(),
    val avatarUrl: String?
)

enum class NotificationType {
    FRIEND_REQUEST,
    NEW_POST,
    NEW_EVENT
}