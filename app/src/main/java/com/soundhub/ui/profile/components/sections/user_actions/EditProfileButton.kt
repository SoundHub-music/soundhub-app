package com.soundhub.ui.profile.components.sections.user_actions

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Person
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController
import com.soundhub.R
import com.soundhub.Route

@Composable
internal fun EditProfileButton(
    modifier: Modifier = Modifier,
    navController: NavHostController
) {
    val buttonContent = stringResource(id = R.string.edit_profile_btn_content)
    val buttonIcon: ImageVector = Icons.Rounded.Person

    ProfileActionLongButton(
        modifier = modifier,
        content = buttonContent,
        buttonIcon = buttonIcon,
        onClick = { navController.navigate(Route.EditUserData.route) }
    )
}