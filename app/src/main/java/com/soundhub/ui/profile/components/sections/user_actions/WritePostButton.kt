package com.soundhub.ui.profile.components.sections.user_actions

import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Create
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
internal fun WritePostButton(modifier: Modifier = Modifier) {
    FilledTonalIconButton(
        onClick = { /* TODO: write create post page */ },
        shape = RoundedCornerShape(10.dp),
        modifier = modifier.size(48.dp)
    ) {
        Icon(imageVector = Icons.Rounded.Create, contentDescription = "create_post_btn")
    }
}