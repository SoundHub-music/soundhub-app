package com.soundhub.ui.profile.components.sections.user_actions.buttons

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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.soundhub.R
import com.soundhub.data.model.User
import com.soundhub.ui.profile.ProfileUiState
import com.soundhub.ui.profile.ProfileViewModel
import kotlinx.coroutines.launch

@Composable
internal fun SendFriendRequestButton(
    modifier: Modifier = Modifier,
    profileViewModel: ProfileViewModel,
    profileOwner: User?
) {
    val profileUiState: ProfileUiState by profileViewModel.profileUiState.collectAsState()
    val isRequestSent: Boolean = profileUiState.isRequestSent

    val coroutineScope = rememberCoroutineScope()
    var buttonIconRes by rememberSaveable { mutableIntStateOf(R.drawable.baseline_person_add) }

    LaunchedEffect(key1 = profileUiState) {
        profileViewModel.checkInvite()
    }

    LaunchedEffect(key1 = isRequestSent) {
        Log.d("SendFriendRequestButton", "was request sent: $isRequestSent")
        buttonIconRes = getSendRequestBtnIconRes(isRequestSent)
//        profileViewModel.loadAllInvitesByAuthorizedUser()
    }

    FilledTonalIconToggleButton(
        modifier = modifier.size(48.dp),
        shape = RoundedCornerShape(10.dp),
        checked = isRequestSent,
        onCheckedChange = {
            coroutineScope.launch {
                onSendRequestButtonClick(
                    profileViewModel = profileViewModel,
                    isRequestSent = isRequestSent,
                    user = profileOwner
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

private fun onSendRequestButtonClick(
    profileViewModel: ProfileViewModel,
    isRequestSent: Boolean,
    user: User?,
) = user?.let {
    if (isRequestSent)
        profileViewModel.deleteInviteToFriends()
    else
        profileViewModel.sendInviteToFriends(recipientId = user.id)
}

private fun getSendRequestBtnIconRes(isRequestSent: Boolean): Int =
    if (isRequestSent)
        R.drawable.baseline_how_to_reg_24
    else
        R.drawable.baseline_person_add