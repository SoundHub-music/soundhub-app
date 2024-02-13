package com.soundhub.ui.profile.components.sections.avatar

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Face
import androidx.compose.material.icons.rounded.Refresh
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.soundhub.R
import com.soundhub.UiStateDispatcher
import com.soundhub.data.datastore.UserPreferences
import com.soundhub.ui.authentication.AuthenticationViewModel
import com.soundhub.utils.Route

@Composable
internal fun UserAvatar(
    navController: NavHostController,
    authViewModel: AuthenticationViewModel = hiltViewModel(),
    uiStateDispatcher: UiStateDispatcher = hiltViewModel(),
    userCreds: UserPreferences?
) {
    // temporary avatar
    val avatar: Painter = painterResource(id = R.drawable.header)
    var selectedImageUri by rememberSaveable { mutableStateOf<Uri?>(null) }
    var isAvatarMenuExpanded by rememberSaveable { mutableStateOf(false) }

    val changeAvatarLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        uri?.let { selectedImageUri = it }
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(0.45f)
    ) {

        Image(
            painter = avatar,
            contentDescription = "avatar",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .matchParentSize()
                .clickable { isAvatarMenuExpanded = !isAvatarMenuExpanded }
        )

        AvatarDropdownMenu(
            modifier = Modifier.align(Alignment.Center),
            isAvatarMenuExpanded = isAvatarMenuExpanded,
            onDismissRequest = { isAvatarMenuExpanded = false },
            onOpenAvatarOptionClick = {
                isAvatarMenuExpanded = false
                /* TODO: make change avatar logic */
            },
            onChangeAvatarOptionClick = {
                isAvatarMenuExpanded = false
                changeAvatarLauncher.launch("image/*")
            }
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 20.dp, start = 16.dp, end = 16.dp),
            horizontalArrangement = Arrangement.End
        ) {
            IconButton(onClick = { navController.navigate(Route.Settings.route) }) {
                Icon(imageVector = Icons.Rounded.Settings, contentDescription = null)
            }
        }
    }
}


@Composable
private fun AvatarDropdownMenu(
    modifier: Modifier = Modifier,
    isAvatarMenuExpanded: Boolean,
    onDismissRequest: () -> Unit = {},
    onOpenAvatarOptionClick: () -> Unit = {},
    onChangeAvatarOptionClick: () -> Unit = {}
) {
    Box(
        modifier = modifier
    ) {
        DropdownMenu(
            expanded = isAvatarMenuExpanded,
            onDismissRequest = onDismissRequest,
            modifier = Modifier
        ) {
            DropdownMenuItem(
                text = { Text(stringResource(id = R.string.menu_action_open_avatar)) },
                leadingIcon = { Icon(imageVector = Icons.Rounded.Face, contentDescription = "open_avatar") },
                onClick = onOpenAvatarOptionClick
            )

            DropdownMenuItem(
                text = { Text(stringResource(id = R.string.menu_action_change_avatar)) },
                leadingIcon = { Icon(imageVector = Icons.Rounded.Refresh, contentDescription = "change_avatar") },
                onClick = onChangeAvatarOptionClick
            )
        }
    }
}