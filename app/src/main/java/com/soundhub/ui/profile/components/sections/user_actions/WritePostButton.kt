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
import androidx.navigation.NavHostController
import com.soundhub.Route

@Composable
internal fun WritePostButton(
    modifier: Modifier = Modifier,
    navController: NavHostController
) {
    FilledTonalIconButton(
        onClick = { navController.navigate(Route.PostEditor.createPostRoute) },
        shape = RoundedCornerShape(10.dp),
        modifier = modifier.size(48.dp)
    ) {
        Icon(imageVector = Icons.Rounded.Create, contentDescription = "create_post_btn")
    }
}