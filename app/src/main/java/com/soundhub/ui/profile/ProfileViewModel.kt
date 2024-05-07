package com.soundhub.ui.profile

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.soundhub.R
import com.soundhub.data.datastore.UserCredsStore
import com.soundhub.data.datastore.UserPreferences
import com.soundhub.data.model.Chat
import com.soundhub.data.model.Invite
import com.soundhub.data.model.User
import com.soundhub.data.repository.InviteRepository
import com.soundhub.data.repository.UserRepository
import com.soundhub.domain.usecases.chat.GetOrCreateChatByUserUseCase
import com.soundhub.domain.usecases.user.GetUserByIdUseCase
import com.soundhub.ui.events.UiEvent
import com.soundhub.ui.states.UiState
import com.soundhub.ui.viewmodels.UiStateDispatcher
import com.soundhub.utils.UiText
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val getOrCreateChatByUserUseCase: GetOrCreateChatByUserUseCase,
    private val inviteRepository: InviteRepository,
    private val userRepository: UserRepository,
    private val uiStateDispatcher: UiStateDispatcher,
    private val getUserByIdUseCase: GetUserByIdUseCase,
    userCredsStore: UserCredsStore
): ViewModel() {
    private val uiState: StateFlow<UiState> = uiStateDispatcher.uiState.asStateFlow()
    private val userCreds: Flow<UserPreferences> = userCredsStore.getCreds()
    val profileUiState: MutableStateFlow<ProfileUiState> = MutableStateFlow(ProfileUiState())

    init {
        viewModelScope.launch {
            initializeProfileState()
        }
    }

    private suspend fun initializeProfileState() {
        uiState.map { it.authorizedUser }
            .collect { user ->
                profileUiState.update { it.copy(authorizedUser = user) }
                loadAllInvitesByAuthorizedUser()
        }
    }

    suspend fun getOrCreateChatByUser(user: User?): Chat? {
        val chat: Chat?
        val creds: UserPreferences? = userCreds.firstOrNull()

        chat = getOrCreateChatByUserUseCase(
            accessToken = creds?.accessToken,
            interlocutor = user
        )
        return chat
    }

    fun deleteInviteToFriends() = viewModelScope.launch(Dispatchers.IO) {
        val text: UiText.StringResource = UiText.StringResource(R.string.toast_invite_deleted_successfully)
        val showToastEvent: UiEvent = UiEvent.ShowToast(text)

        val profileOwner: User? = profileUiState.map { it.profileOwner }
            .firstOrNull()

        val invite: Invite = profileUiState.map { it.invitesSentByCurrentUser }
            .firstOrNull()
            ?.firstOrNull { invite -> invite.recipient == profileOwner }
            ?: return@launch

        inviteRepository.deleteInvite(
            accessToken = userCreds.firstOrNull()?.accessToken,
            inviteId = invite.id
        )
            .onSuccess {
                profileUiState.update { it.copy(isRequestSent = false) }
            }
            .onFailure {
                text.srcId = R.string.toast_delete_invite_error
            }
            .finally {
                uiStateDispatcher.sendUiEvent(showToastEvent)
            }
    }

    fun deleteFriend(user: User) = viewModelScope.launch {
        userRepository.deleteFriend(
            accessToken = userCreds.firstOrNull()?.accessToken,
            friendId = user.id
        )
            .onSuccess { response ->
                Log.d("ProfileViewModel", "deleted friend ${response.body}")
                profileUiState.update { it.copy(isRequestSent = false) }
            }
    }

    fun sendInviteToFriends(recipientId: UUID) = viewModelScope.launch {
        val text: UiText.StringResource = UiText.StringResource(R.string.toast_invite_to_friends_was_sent_successfully)
        val showToastEvent: UiEvent = UiEvent.ShowToast(text)

        inviteRepository.createInvite(
            accessToken = userCreds.firstOrNull()?.accessToken,
            recipientId = recipientId
        ).onSuccess {
            profileUiState.update { it.copy(isRequestSent = true) }
        }.onFailure {
            text.srcId = R.string.toast_common_error
        }
        .finally {
            uiStateDispatcher.sendUiEvent(showToastEvent)
        }
    }

    suspend fun loadAllInvitesByAuthorizedUser() {
        val authorizedUser: Flow<User?> = profileUiState.map { it.authorizedUser }

        authorizedUser.collect { user ->
            user?.let {
                val invites: List<Invite> = inviteRepository.getAllInvitesBySenderId(
                    accessToken = userCreds.firstOrNull()?.accessToken,
                    senderId = user.id
                ).getOrNull() ?: emptyList()

                val profileOwner: User? = profileUiState.map { it.profileOwner }
                    .firstOrNull()

                val isFriendRequestSent: Boolean = invites.map { it.recipient }
                    .contains(profileOwner)

                Log.d("ProfileViewModel", "sent invites: $invites")

                profileUiState.update {
                    it.copy(
                        invitesSentByCurrentUser = invites,
                        isRequestSent = isFriendRequestSent
                    )
                }
            }
        }
    }

    private fun setProfileOwner(user: User?) = viewModelScope.launch(Dispatchers.IO) {
        val authorizedUser: User? = profileUiState
            .map { it.authorizedUser }
            .firstOrNull()

        val isFriend: Boolean = authorizedUser?.friends?.map { it.id }
            ?.contains(user?.id) == true

        profileUiState.update {
            it.copy(
                isUserAFriendToAuthorizedUser = isFriend,
                profileOwner = user
            )
        }
    }

    fun loadProfileOwner(id: UUID) = viewModelScope.launch(Dispatchers.IO) {
        val authorizedUser: User? = profileUiState.map { it.authorizedUser }
            .firstOrNull()
        val profileOwner: User? = if (authorizedUser?.id == id)
            authorizedUser
        else getUserByIdUseCase(id)

        setProfileOwner(profileOwner)
    }
}