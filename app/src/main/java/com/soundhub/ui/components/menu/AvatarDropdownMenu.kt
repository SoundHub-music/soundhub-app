package com.soundhub.ui.components.menu

import android.net.Uri
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.compose.foundation.layout.Box
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Face
import androidx.compose.material.icons.rounded.Refresh
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.soundhub.R
import com.soundhub.utils.ContentTypes

@Composable
internal fun AvatarDropdownMenu(
    modifier: Modifier = Modifier,
    isAvatarMenuExpandedState: MutableState<Boolean>,
    activityResultLauncher: ManagedActivityResultLauncher<String, Uri?>,
    onDismissRequest: () -> Unit = {}
) {
    Box(
        modifier = modifier
    ) {
        DropdownMenu(
            expanded = isAvatarMenuExpandedState.value,
            onDismissRequest = onDismissRequest,
            modifier = Modifier
        ) {
            DropdownMenuItem(
                text = { Text(stringResource(id = R.string.menu_action_open_avatar)) },
                leadingIcon = { Icon(imageVector = Icons.Rounded.Face, contentDescription = "open_avatar") },
                onClick = {
                    onOpenAvatarMenuClick()
                    isAvatarMenuExpandedState.value = false
                }
            )

            DropdownMenuItem(
                text = { Text(stringResource(id = R.string.menu_action_change_avatar)) },
                leadingIcon = { Icon(imageVector = Icons.Rounded.Refresh, contentDescription = "change_avatar") },
                onClick = {
                    onChangeAvatarMenuClick(activityResultLauncher)
                    isAvatarMenuExpandedState.value = false
                }
            )
        }
    }
}

private fun onOpenAvatarMenuClick() {
    /* TODO: implement open avatar logic */
}

private fun onChangeAvatarMenuClick(
    changeAvatarLauncher: ManagedActivityResultLauncher<String, Uri?>
) {
    changeAvatarLauncher.launch(ContentTypes.IMAGE_ALL.type)
}