package com.soundhub.ui.shared.buttons

import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowForward
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.soundhub.ui.shared.loaders.CircleLoader

@Composable
fun FloatingNextButton(
    modifier: Modifier = Modifier,
    isLoading: Boolean = false,
    onClick: () -> Unit,
) {
    FloatingActionButton(
        onClick = onClick,
        shape = RoundedCornerShape(16.dp),
        containerColor = MaterialTheme.colorScheme.primary,
        modifier = modifier
    ) {
        if (isLoading) CircleLoader(modifier = Modifier.size(20.dp))
        else Icon(
            imageVector = Icons.AutoMirrored.Rounded.ArrowForward,
            contentDescription = "arrow_forward"
        )
    }
}

@Preview
@Composable
private fun NextButtonPreview() {
    FloatingNextButton {}
}