package com.soundhub.ui.friends

import com.soundhub.data.enums.ApiStatus
import com.soundhub.data.model.User

data class FriendsUiState(
    val recommendedFriends: List<User> = emptyList(),
    val profileOwner: User? = null,
    val foundUsers: List<User> = emptyList(),
    val status: ApiStatus = ApiStatus.LOADING
)