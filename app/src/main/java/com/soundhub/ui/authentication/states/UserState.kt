package com.soundhub.ui.authentication.states

import com.soundhub.data.enums.ApiStatus
import com.soundhub.data.model.User

data class UserState(
    val current: User? = null,
    val status: ApiStatus = ApiStatus.LOADING
)
