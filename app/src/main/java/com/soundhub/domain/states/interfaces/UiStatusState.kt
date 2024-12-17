package com.soundhub.domain.states.interfaces

import com.soundhub.data.enums.ApiStatus

interface UiStatusState {
	val status: ApiStatus
}