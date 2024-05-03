package com.soundhub.ui.profile

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.soundhub.R
import com.soundhub.data.dao.UserDao
import com.soundhub.data.database.AppDatabase
import com.soundhub.data.datastore.UserCredsStore
import com.soundhub.data.datastore.UserPreferences
import com.soundhub.data.model.Chat
import com.soundhub.data.model.Invite
import com.soundhub.data.model.User
import com.soundhub.data.repository.InviteRepository
import com.soundhub.domain.usecases.chat.GetOrCreateChatByUserUseCase
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
class ProfileViewModel @Inject constructor(
    private val getOrCreateChatByUserUseCase: GetOrCreateChatByUserUseCase,
    private val inviteRepository: InviteRepository,
    private val uiStateDispatcher: UiStateDispatcher,
    userCredsStore: UserCredsStore,
    appDb: AppDatabase
): ViewModel() {
    private val userCreds: Flow<UserPreferences> = userCredsStore.getCreds()
    private val userDao: UserDao = appDb.userDao()
    val authorizedUserState: MutableStateFlow<User?> = MutableStateFlow(null)
    val authorizedUserInvitesState: MutableStateFlow<List<Invite>> = MutableStateFlow(emptyList())

    init {
        viewModelScope.launch {
            authorizedUserState.update { userDao.getCurrentUser() }
            loadAllInvitesByAuthorizedUserId()
        }
    }

    suspend fun getOrCreateChatByUser(user: User?): Chat? {
        var chat: Chat? = null
        userCreds.collect { creds ->
            chat = getOrCreateChatByUserUseCase(
                accessToken = creds.accessToken,
                interlocutor = user
            )
        }
        return chat
    }

    fun deleteInviteToFriends() = viewModelScope.launch {
//        var text: UiText = UiText.StringResource()
    }

    fun sendInviteToFriends(recipientId: UUID) = viewModelScope.launch(Dispatchers.IO) {
        var text: UiText = UiText.StringResource(R.string.toast_invite_to_friends_was_sent_successfully)

        userCreds.collect { creds ->
            inviteRepository.createInvite(
                accessToken = creds.accessToken,
                recipientId = recipientId
            ).onSuccess { response ->
                Log.d("ProfileViewModel", "sendInviteToFriends[1]: $response")
            }.onFailure { error ->
                text = UiText.StringResource(R.string.toast_common_error)
                Log.e("ProfileViewModel", "sendInviteToFriends[2]: $error")
            }
            .finally {
                val uiEvent: UiEvent = UiEvent.ShowToast(text)
                uiStateDispatcher.sendUiEvent(uiEvent)
            }
        }
    }

    private suspend fun loadAllInvitesByAuthorizedUserId() {
        userCreds.collect { creds ->
            authorizedUserState.value?.let { user ->
                val invites = inviteRepository.getAllInvitesBySenderId(
                    accessToken = creds.accessToken,
                    senderId = user.id
                ).getOrNull() ?: emptyList()
                authorizedUserInvitesState.update { invites }
            }
        }

    }

}