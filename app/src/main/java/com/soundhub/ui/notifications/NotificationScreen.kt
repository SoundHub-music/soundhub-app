package com.soundhub.ui.notifications

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.soundhub.ui.components.containers.ContentContainer
import com.soundhub.ui.notifications.components.Notification
import com.soundhub.ui.notifications.components.NotificationItem
import com.soundhub.ui.notifications.components.NotificationType

@Composable
fun NotificationScreen(
    navController: NavHostController
) {
    val notifications = listOf(
        Notification(
            avatarUrl = null,
            type = NotificationType.FRIEND_REQUEST,
            objectName = "User"
        )
    )


    ContentContainer(
        modifier = Modifier.padding(top = 10.dp),
        contentAlignment = if (notifications.isEmpty()) Alignment.Center
        else Alignment.TopStart
    ) {
        if (notifications.isEmpty()) Text(
            text = "Здесь появятся ваши уведомления",
            textAlign = TextAlign.Center,
            style = TextStyle(
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
            )
        )

        LazyColumn(
            modifier = Modifier.fillMaxSize()
        ) {
            items(items = notifications, key = { it.id }) { n ->
                NotificationItem(
                    avatarUrl = n.avatarUrl,
                    type = n.type,
                    objectName = n.objectName
                )
            }
        }
    }
}

@Composable
@Preview
fun NotificationScreenPreview() {
    val navController = rememberNavController()
    NotificationScreen(navController)
}