package com.soundhub.data.states

import com.soundhub.data.api.responses.CompatibleUsersResponse
import com.soundhub.data.enums.ApiStatus
import com.soundhub.data.model.User

data class FriendsUiState(
	val recommendedFriends: List<User> = emptyList(),
	val userCompatibilityPercentage: CompatibleUsersResponse? = null,
	val profileOwner: User? = null,
	val isOriginProfile: Boolean = true,
	val foundUsers: List<User> = emptyList(),
	val status: ApiStatus = ApiStatus.LOADING
)