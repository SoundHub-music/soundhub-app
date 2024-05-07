package com.soundhub.domain.usecases.user

import com.soundhub.R
import com.soundhub.data.model.User
import com.soundhub.data.repository.UserRepository
import com.soundhub.ui.events.UiEvent
import com.soundhub.ui.viewmodels.UiStateDispatcher
import com.soundhub.utils.UiText
import javax.inject.Inject

class UpdateUserUseCase @Inject constructor(
    private val userRepository: UserRepository,
    private val uiStateDispatcher: UiStateDispatcher
) {
    suspend operator fun invoke(user: User?, accessToken: String?) {
        userRepository
            .updateUserById(accessToken, user)
            .onFailure {
                val toastText = UiText.StringResource(R.string.toast_update_error)
                uiStateDispatcher.sendUiEvent(UiEvent.ShowToast(toastText))
            }
    }
}