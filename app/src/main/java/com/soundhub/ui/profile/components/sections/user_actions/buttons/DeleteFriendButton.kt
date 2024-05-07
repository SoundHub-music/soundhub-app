package com.soundhub.ui.profile.components.sections.user_actions.buttons

import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.soundhub.R
import com.soundhub.data.model.User
import com.soundhub.ui.profile.ProfileViewModel

@Composable
fun DeleteFriendButton(
    profileViewModel: ProfileViewModel,
    authorizedUser: User?,
    user: User?
) {
    FilledTonalIconButton(
        modifier = Modifier.size(48.dp),
        shape = RoundedCornerShape(10.dp),
        onClick = {
            onDeleteFriendBtnClick(
                profileViewModel = profileViewModel,
                authorizedUser = authorizedUser,
                user = user
            )
        }
    ) {
        Icon(
            painter = painterResource(R.drawable.person_remove_24),
            contentDescription = "delete friend button"
        )
    }
}

private fun onDeleteFriendBtnClick(
    profileViewModel: ProfileViewModel,
    authorizedUser: User?,
    user: User?
) = user?.let {
    if (authorizedUser?.friends?.contains(user) == true)
        profileViewModel.deleteFriend(user)
}