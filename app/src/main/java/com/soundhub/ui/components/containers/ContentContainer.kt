package com.soundhub.ui.components.containers

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun ContentContainer(modifier: Modifier = Modifier, content: @Composable () -> Unit = {}) {
    Box(
        modifier = modifier
            .padding(
                vertical = 10.dp,
                horizontal = 16.dp,
            )
    ) {
        content()
    }
}