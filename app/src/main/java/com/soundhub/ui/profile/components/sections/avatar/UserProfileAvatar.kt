package com.soundhub.ui.profile.components.sections.avatar

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.runtime.collectAsState
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
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.soundhub.R
import com.soundhub.ui.authentication.AuthenticationViewModel
import com.soundhub.Route
import com.soundhub.data.model.User
import com.soundhub.utils.MediaTypes
import java.io.File

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
internal fun UserProfileAvatar(
    navController: NavHostController,
    authViewModel: AuthenticationViewModel = hiltViewModel(),
    user: User? = null
) {
    val defaultAvatar: Painter = painterResource(id = R.drawable.circular_user)
    val userAvatar: File? by authViewModel.currentUserAvatar.collectAsState()
    var selectedImageUri by rememberSaveable { mutableStateOf<Uri?>(null) }
    var isAvatarMenuExpanded by rememberSaveable { mutableStateOf(false) }

    val changeAvatarLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri -> uri?.let { selectedImageUri = it } }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(0.45f)
    ) {
        if (userAvatar == null)
            Image(
                painter = defaultAvatar,
                contentDescription = null,
                modifier = Modifier.fillMaxWidth(),
                contentScale = ContentScale.Crop
            )
        else GlideImage(
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop,
            model = userAvatar,
            contentDescription = "${user?.firstName} ${user?.lastName}".trim()
        )

        AvatarDropdownMenu(
            modifier = Modifier.align(Alignment.Center),
            isAvatarMenuExpanded = isAvatarMenuExpanded,
            onDismissRequest = { isAvatarMenuExpanded = false },
            onOpenAvatarOptionClick = {
                isAvatarMenuExpanded = false
                /* TODO: implement change avatar logic */
            },
            onChangeAvatarOptionClick = {
                isAvatarMenuExpanded = false
                changeAvatarLauncher.launch(MediaTypes.IMAGE_ALL.type)
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