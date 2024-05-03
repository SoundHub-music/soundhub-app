package com.soundhub.ui.profile.components.sections.user_actions

import android.util.Log
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.FilledTonalIconToggleButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.soundhub.R
import com.soundhub.data.model.Invite
import com.soundhub.data.model.User
import com.soundhub.ui.profile.ProfileViewModel

@Composable
internal fun SendFriendRequestButton(
    modifier: Modifier = Modifier,
    profileViewModel: ProfileViewModel,
    user: User?
) {
    val authorizedUserInvites: List<Invite> by profileViewModel
        .authorizedUserInvitesState
        .collectAsState()

    var isRequestSent by rememberSaveable { mutableStateOf(false) }
    val defaultIconRes: Int = getButtonIconRes(isRequestSent = isRequestSent)
    var buttonIconRes by rememberSaveable { mutableIntStateOf(defaultIconRes) }

    LaunchedEffect(key1 = isRequestSent) {
        buttonIconRes = getButtonIconRes(isRequestSent)
    }

    LaunchedEffect(key1 = authorizedUserInvites) {
        Log.d("SendFriendRequestButton", "invites: $authorizedUserInvites")
        isRequestSent = hasInvite(invites = authorizedUserInvites, user = user)
    }

    // TODO: make delete friend button
    FilledTonalIconToggleButton(
        modifier = modifier.size(48.dp),
        shape = RoundedCornerShape(10.dp),
        checked = isRequestSent,
        onCheckedChange = {
            isRequestSent = !isRequestSent
            user?.let {
                profileViewModel.sendInviteToFriends(
                    recipientId = it.id
                )
            }
        },
    ) {
        Icon(
            painter = painterResource(id = buttonIconRes),
            contentDescription = "add friend button"
        )
    }
}

private fun hasInvite(invites: List<Invite>, user: User?): Boolean {
    val invitedUsers: List<User> = invites.map { it.recipient }
    return user in invitedUsers
}

private fun getButtonIconRes(isRequestSent: Boolean): Int =
    if (isRequestSent) R.drawable.baseline_how_to_reg_24
    else R.drawable.baseline_person_add