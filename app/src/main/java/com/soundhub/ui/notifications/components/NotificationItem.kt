package com.soundhub.ui.notifications.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.unit.dp
import com.soundhub.data.model.Notification

@Composable
fun NotificationItem(
    modifier: Modifier = Modifier,
    notification: Notification,
    onClick: () -> Unit = {},
    content: @Composable () -> Unit
) {
    ElevatedCard(
        shape = RoundedCornerShape(10.dp),
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
        Box(modifier = Modifier.padding(20.dp)) {
            content()
        }
    }
}