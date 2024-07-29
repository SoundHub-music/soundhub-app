package com.soundhub.ui.pages.music.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.unit.dp

@Composable
fun MusicServiceButton(
    modifier: Modifier = Modifier,
    painter: Painter,
    contentDescription: String? = null,
    onClick: () -> Unit = {},
) {
    IconButton(
        onClick = onClick,
        modifier = modifier
            .shadow(
                elevation = 4.dp,
                shape = RoundedCornerShape(10.dp),
                spotColor = Color.Black
            )
            .background(
                color = MaterialTheme.colorScheme.primaryContainer,
                shape = RoundedCornerShape(10.dp)
            )
            .padding(5.dp)
            .size(70.dp)
    ) {
        Image(
            painter = painter,
            contentDescription = contentDescription,
            modifier = Modifier.size(48.dp)
        )
    }
}