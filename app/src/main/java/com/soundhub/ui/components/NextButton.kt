package com.soundhub.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowForward
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun NextButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) {

    IconButton(
        onClick = onClick,
        modifier = modifier
            .width(56.dp)
            .height(56.dp)
            .background(MaterialTheme.colorScheme.primary, RoundedCornerShape(size = 16.dp))
    ) {
        Icon(
            imageVector = Icons.Rounded.ArrowForward,
            contentDescription = "arrow_forward"
        )
    }
}

@Preview
@Composable
fun viewBtn() {
    NextButton {

    }
}