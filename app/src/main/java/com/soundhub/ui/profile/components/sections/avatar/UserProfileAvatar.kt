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
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
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
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.soundhub.R
import com.soundhub.Route
import com.soundhub.data.model.User
import com.soundhub.ui.components.menu.AvatarDropdownMenu
import com.soundhub.ui.profile.ProfileViewModel
import java.io.File

@Composable
internal fun UserProfileAvatar(
    navController: NavHostController,
    profileViewModel: ProfileViewModel,
    user: User?
) {
    var selectedImageUri: Uri? by rememberSaveable { mutableStateOf(null) }
    val authorizedUser: User? by profileViewModel
        .authorizedUserState
        .collectAsState()

    val isAuthorizedUser: Boolean = authorizedUser?.id == user?.id
    val isAvatarMenuExpandedState: MutableState<Boolean> = rememberSaveable {
        mutableStateOf(false)
    }

    val changeAvatarLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri -> uri?.let { selectedImageUri = it } }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(0.45f)
    ) {
        Avatar(isAvatarMenuExpandedState, user)
        AvatarDropdownMenu(
            modifier = Modifier.align(Alignment.Center),
            isAvatarMenuExpandedState = isAvatarMenuExpandedState,
            onDismissRequest = { isAvatarMenuExpandedState.value = false },
            activityResultLauncher = changeAvatarLauncher
        )
        UserSettingsButton(
            isAuthorizedUser = isAuthorizedUser,
            navController = navController
        )
    }
}

@Composable
private fun UserSettingsButton(isAuthorizedUser: Boolean, navController: NavHostController) {
    if (isAuthorizedUser) Row(
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

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
private fun Avatar(
    isAvatarMenuExpandedState: MutableState<Boolean>,
    user: User?
) {
    val userAvatar: File? = user?.avatarImageFile
    val defaultAvatar: Painter = painterResource(id = R.drawable.circular_user)
    
    if (userAvatar == null)
        Image(
            painter = defaultAvatar,
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth()
                .clickable { isAvatarMenuExpandedState.value = true },
            contentScale = ContentScale.Crop
        )
    else GlideImage(
        modifier = Modifier
            .fillMaxSize()
            .clickable { isAvatarMenuExpandedState.value = true },
        contentScale = ContentScale.Crop,
        model = userAvatar,
        contentDescription = "${user.firstName} ${user.lastName}".trim()
    )
}