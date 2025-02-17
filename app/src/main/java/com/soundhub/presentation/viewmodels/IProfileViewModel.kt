package com.soundhub.presentation.viewmodels

import com.soundhub.domain.states.IProfileUiState
import kotlinx.coroutines.flow.StateFlow

interface IProfileViewModel<T> {
	val profileUiState: StateFlow<IProfileUiState<T>>

	fun getUserName(): String
	fun getAvatarModel(): Any?
}