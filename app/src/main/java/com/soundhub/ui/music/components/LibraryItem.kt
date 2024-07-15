package com.soundhub.ui.music.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.soundhub.R

@Composable
internal fun LibraryItem(
    modifier: Modifier = Modifier,
    title: String,
    icon: Painter,
    contentDescription: String? = null,
    onClick: () -> Unit = {}
) {
    ElevatedCard(
        shape = RoundedCornerShape(10.dp),
        onClick = onClick,
        modifier = modifier
            .padding(5.dp)
            .fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        )
    ) {
        Row(
            modifier = Modifier
                .padding(vertical = 15.dp, horizontal = 10.dp),
            horizontalArrangement = Arrangement.spacedBy(15.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .background(
                        color = MaterialTheme.colorScheme.secondaryContainer,
                        shape = RoundedCornerShape(10.dp)
                    )
                    .padding(5.dp)
            ) {
                Icon(
                    painter = icon,
                    contentDescription = contentDescription,
                    tint = MaterialTheme.colorScheme.tertiary,
                    modifier = Modifier.padding(5.dp)
                )
            }
            Text(
                text = title,
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

@Composable
@Preview(showBackground = true)
private fun LibraryItemPreview() {
    Box(modifier = Modifier.padding(10.dp)) {
        LibraryItem(
            title = "Плейлисты",
            icon = painterResource(id = R.drawable.baseline_album_24),
        )
    }
}