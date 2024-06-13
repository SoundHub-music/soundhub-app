package com.soundhub.ui.profile

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.soundhub.R
import com.soundhub.data.datastore.UserCredsStore
import com.soundhub.data.datastore.UserPreferences
import com.soundhub.data.enums.ApiStatus
import com.soundhub.data.model.Chat
import com.soundhub.data.model.Invite
import com.soundhub.data.model.Post
import com.soundhub.data.model.User
import com.soundhub.data.repository.InviteRepository
import com.soundhub.data.repository.UserRepository
import com.soundhub.domain.usecases.chat.GetOrCreateChatByUserUseCase
import com.soundhub.domain.usecases.post.DeletePostByIdUseCase
import com.soundhub.domain.usecases.post.GetPostsByUserUseCase
import com.soundhub.domain.usecases.post.TogglePostLikeAndUpdateListUseCase
import com.soundhub.domain.usecases.user.GetUserByIdUseCase
import com.soundhub.ui.events.UiEvent
import com.soundhub.ui.states.UiState
import com.soundhub.ui.viewmodels.UiStateDispatcher
import com.soundhub.utils.UiText
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val uiStateDispatcher: UiStateDispatcher,
    private val inviteRepository: InviteRepository,

    private val togglePostLikeAndUpdateListUseCase: TogglePostLikeAndUpdateListUseCase,
    private val getOrCreateChatByUserUseCase: GetOrCreateChatByUserUseCase,
    private val getPostsByUserUseCase: GetPostsByUserUseCase,
    private val deletePostByIdUseCase: DeletePostByIdUseCase,
    private val getUserByIdUseCase: GetUserByIdUseCase,
    userCredsStore: UserCredsStore
): ViewModel() {
    private val uiState: Flow<UiState> = uiStateDispatcher.uiState
    private val userCreds: Flow<UserPreferences> = userCredsStore.getCreds()

    private val _profileUiState: MutableStateFlow<ProfileUiState> = MutableStateFlow(ProfileUiState())
    val profileUiState = _profileUiState.asStateFlow()

    init { initializeProfileState() }

    private fun initializeProfileState() = viewModelScope.launch(Dispatchers.Main) {
        _profileUiState.update { it.copy(
            userCreds = userCreds.firstOrNull()
        ) }
    }

    suspend fun getOrCreateChatByUser(user: User?): Flow<Chat?> = flow {
        val ( authorizedUser: User? ) = _profileUiState.value
        emit(authorizedUser?.let {
            getOrCreateChatByUserUseCase(
                interlocutor = user,
                userId = authorizedUser.id
            )
        })
    }

    fun deleteInviteToFriends() = viewModelScope.launch(Dispatchers.IO) {
        val text: UiText.StringResource = UiText.StringResource(R.string.toast_invite_deleted_successfully)
        val showToastEvent: UiEvent = UiEvent.ShowToast(text)

        val profileOwner: User? = _profileUiState.map { it.profileOwner }
            .firstOrNull()

        val invite: Invite = _profileUiState.map { it.inviteSentByCurrentUser }
            .firstOrNull()
            ?.takeIf { it.recipient.id == profileOwner?.id }
            ?: return@launch

        inviteRepository.deleteInvite(invite.id)
            .onSuccessWithContext {
                _profileUiState.update { it.copy(isRequestSent = false) }
            }
            .onFailureWithContext { error ->
                val errorEvent: UiEvent = UiEvent.Error(
                    error.errorBody,
                    error.throwable,
                    R.string.toast_delete_invite_error
                )
                uiStateDispatcher.sendUiEvent(errorEvent)
            }
            .finallyWithContext {
                uiStateDispatcher.sendUiEvent(showToastEvent)
            }
    }

    fun deleteFriend(user: User) = viewModelScope.launch(Dispatchers.IO) {
        userRepository.deleteFriend(user.id)
            .onSuccessWithContext { response ->
                Log.d("ProfileViewModel", "deleted friend ${response.body}")
                _profileUiState.update { it.copy(isRequestSent = false) }
            }
    }

    fun sendInviteToFriend(recipientId: UUID) = viewModelScope.launch(Dispatchers.IO) {
        val text: UiText.StringResource = UiText.StringResource(R.string.toast_invite_to_friends_was_sent_successfully)
        val showToastEvent: UiEvent = UiEvent.ShowToast(text)

        inviteRepository.createInvite(recipientId).onSuccessWithContext {
            _profileUiState.update { it.copy(isRequestSent = true) }
        }.onFailureWithContext { error ->
            val errorEvent: UiEvent = UiEvent.Error(
                error.errorBody,
                error.throwable
            )
            uiStateDispatcher.sendUiEvent(errorEvent)
        }
        .finallyWithContext {
            uiStateDispatcher.sendUiEvent(showToastEvent)
        }
    }

    fun checkInvite() = viewModelScope.launch(Dispatchers.IO) {
        val authorizedUser: Flow<User?> = uiState.map { it.authorizedUser }
        val profileOwner: User? = _profileUiState.value.profileOwner

        authorizedUser.collect { user ->
            val invite: Invite? = inviteRepository.getInviteBySenderAndRecipientId(
                senderId = user?.id,
                recipientId = profileOwner?.id
            ).getOrNull()

            val isRequestSent: Boolean = invite?.recipient?.id == profileOwner?.id
            val inviteSentByCurrentUser: Invite? = if (isRequestSent) invite else null


            withContext(Dispatchers.Main) {
                _profileUiState.update { it.copy(
                    isRequestSent = isRequestSent,
                    inviteSentByCurrentUser = inviteSentByCurrentUser
                ) }
            }
        }
    }

    fun loadProfileOwner(id: UUID) = viewModelScope.launch(Dispatchers.Main) {
        val authorizedUser: User? = uiState.map { it.authorizedUser }.firstOrNull()
        val profileOwner: User? = if (authorizedUser?.id == id)
            authorizedUser
        else getUserByIdUseCase(id)

        setProfileOwner(profileOwner, authorizedUser)
    }

    private fun setProfileOwner(
        profileOwner: User?,
        authorizedUser: User?
    ) {
        if (profileOwner?.id == null || authorizedUser?.id == null)
            return

        val isFriend: Boolean = authorizedUser
            .friends.any { it.id == profileOwner.id }

        _profileUiState.update { state ->
            state.copy(
                isUserAFriendToAuthorizedUser = isFriend,
                profileOwner = profileOwner
            )
        }
    }

    fun loadPostsByUser() = viewModelScope.launch(Dispatchers.IO) {
        val ( profileOwner: User? ) = _profileUiState.value
        val posts: List<Post> = _profileUiState.value.userPosts

        if (posts.isEmpty())
            _profileUiState.update { it.copy(postStatus = ApiStatus.LOADING) }

        profileOwner?.let {
            getPostsByUserUseCase(profileOwner)
                .onSuccess { response ->
                    withContext(Dispatchers.Main) {
                        _profileUiState.update {
                            it.copy(
                                userPosts = response,
                                postStatus = ApiStatus.SUCCESS
                            )
                        }
                    }
                }
                .onFailure {
                    withContext(Dispatchers.Main) {
                        _profileUiState.update { it.copy(postStatus = ApiStatus.ERROR) }
                    }
                }
        }
    }

    fun deletePostById(postId: UUID) = viewModelScope.launch(Dispatchers.IO) {
        deletePostByIdUseCase(postId)
            .onSuccess { response ->
                withContext(Dispatchers.Main) {
                    _profileUiState.update { state ->
                        val deletedPostId: UUID? = response
                        state.copy(
                            userPosts = state.userPosts.filter { deletedPostId != it.id },
                            postStatus = ApiStatus.SUCCESS
                        )
                    }
                }
            }
            .onFailure {
                withContext(Dispatchers.Main) {
                    _profileUiState.update { it.copy(postStatus = ApiStatus.ERROR) }
                }
            }
    }

    fun togglePostLikeAndUpdatePostList(postId: UUID) = viewModelScope.launch(Dispatchers.IO) {
        val posts: List<Post> = _profileUiState.value.userPosts
        togglePostLikeAndUpdateListUseCase(postId, posts)
            .onSuccess { response ->
                withContext(Dispatchers.Main) {
                    _profileUiState.update {
                        val updatedPostList: List<Post> = response
                        it.copy(userPosts = updatedPostList)
                    }
                }
            }
            .onFailure { error ->
                withContext(Dispatchers.Main) {
                    error.message?.let {
                        val toastText: UiText = UiText.DynamicString(it)
                        uiStateDispatcher.sendUiEvent(UiEvent.ShowToast(toastText))
                    }
                }
            }
    }
}