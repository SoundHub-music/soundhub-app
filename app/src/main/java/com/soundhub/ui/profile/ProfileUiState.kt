package com.soundhub.ui.profile

import com.soundhub.data.datastore.UserPreferences
import com.soundhub.data.model.Invite
import com.soundhub.data.model.User

data class ProfileUiState(
    val profileOwner: User? = null,
    val isRequestSent: Boolean = false,
    val isUserAFriendToAuthorizedUser: Boolean = false,
    val inviteSentByCurrentUser: Invite? = null,
    val userCreds: UserPreferences? = null
)
