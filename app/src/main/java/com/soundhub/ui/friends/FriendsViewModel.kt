package com.soundhub.ui.friends

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.soundhub.data.datastore.UserCredsStore
import com.soundhub.data.datastore.UserPreferences
import com.soundhub.data.enums.ApiStatus
import com.soundhub.data.repository.UserRepository
import com.soundhub.ui.events.UiEvent
import com.soundhub.ui.viewmodels.UiStateDispatcher
import com.soundhub.utils.UiText
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class FriendsViewModel @Inject constructor(
    userCredsStore: UserCredsStore,
    private val userRepository: UserRepository,
    private val uiStateDispatcher: UiStateDispatcher
): ViewModel() {
    private val userCreds: Flow<UserPreferences> = userCredsStore.getCreds()
    val friendsUiState: MutableStateFlow<FriendsUiState> = MutableStateFlow(FriendsUiState())

    fun loadRecommendedFriends(userId: UUID?) = viewModelScope.launch(Dispatchers.IO) {
        userCreds.collect { creds ->
            userRepository.getRecommendedFriends(
                accessToken = creds.accessToken,
                userId = userId
            )
                .onSuccess { response ->
                    friendsUiState.update {
                        it.copy(
                            recommendedFriends = response.body ?: emptyList(),
                            status = ApiStatus.SUCCESS
                        )
                    }
                }
                .onFailure { error ->
                    error.errorBody.detail?.let {
                        uiStateDispatcher.sendUiEvent(UiEvent.ShowToast(
                            UiText.DynamicString(it)
                        ))
                    }
                    friendsUiState.update { it.copy(status = ApiStatus.ERROR) }
                }
        }
    }
}