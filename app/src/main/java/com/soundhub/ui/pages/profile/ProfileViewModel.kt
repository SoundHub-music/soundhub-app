package com.soundhub.ui.pages.profile

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.soundhub.R
import com.soundhub.Route
import com.soundhub.data.dao.PostDao
import com.soundhub.data.dao.UserDao
import com.soundhub.data.datastore.UserCredsStore
import com.soundhub.data.datastore.model.UserPreferences
import com.soundhub.data.enums.ApiStatus
import com.soundhub.data.model.Chat
import com.soundhub.data.model.Invite
import com.soundhub.data.model.Post
import com.soundhub.data.model.User
import com.soundhub.data.repository.InviteRepository
import com.soundhub.data.repository.UserRepository
import com.soundhub.data.states.ProfileUiState
import com.soundhub.domain.usecases.chat.GetOrCreateChatByUserUseCase
import com.soundhub.domain.usecases.post.DeletePostByIdUseCase
import com.soundhub.domain.usecases.post.GetPostsByUserUseCase
import com.soundhub.domain.usecases.post.TogglePostLikeAndUpdateListUseCase
import com.soundhub.domain.usecases.user.GetUserByIdUseCase
import com.soundhub.ui.events.UiEvent
import com.soundhub.ui.viewmodels.UiStateDispatcher
import com.soundhub.utils.lib.UiText
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
	private val userDao: UserDao,
	private val postDao: PostDao,
	userCredsStore: UserCredsStore
) : ViewModel() {
	private val userCreds: Flow<UserPreferences> = userCredsStore.getCreds()

	private val _profileUiState: MutableStateFlow<ProfileUiState> =
		MutableStateFlow(ProfileUiState())
	val profileUiState = _profileUiState.asStateFlow()

	init {
		initializeProfileState()
	}

	private fun initializeProfileState() = viewModelScope.launch(Dispatchers.Main) {
		_profileUiState.update {
			it.copy(
				userCreds = userCreds.firstOrNull()
			)
		}
	}

	fun getOrCreateChatByUser(user: User?): Flow<Chat?> = flow {
		val (authorizedUser: User?) = _profileUiState.value
		emit(authorizedUser?.let {
			getOrCreateChatByUserUseCase(
				interlocutor = user,
				userId = authorizedUser.id
			)
		})
	}

	// on click button listeners
	fun onDeleteFriendBtnClick(user: User?) = viewModelScope.launch(Dispatchers.IO) {
		user?.let {
			val authorizedUser: User? = userDao.getCurrentUser()
			if (authorizedUser?.friends?.map { it.id }.orEmpty().contains(user.id))
				deleteFriend(user)
		}
	}

	fun onFriendSectionClick() = viewModelScope.launch(Dispatchers.Main) {
		val (profileOwner: User?) = _profileUiState.value
		val userId: String = profileOwner?.id.toString()
		val friendPage: Route = Route.Profile.Friends.getRouteWithNavArg(userId)
		uiStateDispatcher.sendUiEvent(UiEvent.Navigate(friendPage))
	}


	// invite logic
	private fun sendInviteToFriend(recipientId: UUID) = viewModelScope.launch(Dispatchers.IO) {
		val text: UiText.StringResource =
			UiText.StringResource(R.string.toast_invite_to_friends_was_sent_successfully)
		var toastEvent: UiEvent = UiEvent.ShowToast(text)

		inviteRepository.createInvite(recipientId)
			.onSuccessWithContext { _profileUiState.update { it.copy(isRequestSent = true) } }
			.onFailureWithContext { error ->
				toastEvent = UiEvent.Error(error.errorBody, error.throwable)
			}
			.finallyWithContext { uiStateDispatcher.sendUiEvent(toastEvent) }
	}

	fun onSendRequestButtonClick(
		isRequestSent: Boolean,
		user: User?,
	) = user?.let {
		if (isRequestSent)
			deleteInviteToFriends()
		else sendInviteToFriend(recipientId = user.id)
	}

	private fun deleteInviteToFriends() = viewModelScope.launch(Dispatchers.IO) {
		val text: UiText.StringResource =
			UiText.StringResource(R.string.toast_invite_deleted_successfully)
		var toastEvent: UiEvent = UiEvent.ShowToast(text)

		val profileOwner: User? = _profileUiState.map { it.profileOwner }.firstOrNull()

		val invite: Invite = _profileUiState.map { it.inviteSentByCurrentUser }
			.firstOrNull()
			?.takeIf { it.recipient.id == profileOwner?.id }
			?: return@launch

		inviteRepository.deleteInvite(invite.id)
			.onSuccessWithContext {
				_profileUiState.update { it.copy(isRequestSent = false) }
			}
			.onFailureWithContext { error ->
				toastEvent = UiEvent.Error(
					error.errorBody,
					error.throwable,
					R.string.toast_delete_invite_error
				)
			}
			.finallyWithContext { uiStateDispatcher.sendUiEvent(toastEvent) }
	}

	fun checkInvite() = viewModelScope.launch(Dispatchers.IO) {
		val authorizedUser: User? = userDao.getCurrentUser()
		val profileOwner: User? = _profileUiState.value.profileOwner

		if (authorizedUser != null && profileOwner?.id == authorizedUser.id) {
			inviteRepository.getInviteBySenderAndRecipientId(
				senderId = authorizedUser.id,
				recipientId = profileOwner.id
			)
				.onSuccessWithContext { response ->
					val invite = response.body
					val isRequestSent: Boolean = invite?.recipient?.id == profileOwner.id
					val inviteSentByCurrentUser: Invite? = if (isRequestSent) invite else null
					_profileUiState.update {
						it.copy(
							isRequestSent = isRequestSent,
							inviteSentByCurrentUser = inviteSentByCurrentUser
						)
					}
				}
				.onFailureWithContext { error ->
					if (error.errorBody.status != 404) {
						val uiEvent = UiEvent.Error(error.errorBody, error.throwable)
						uiStateDispatcher.sendUiEvent(uiEvent)
					}
				}
		}
	}

	private fun deleteFriend(user: User) = viewModelScope.launch(Dispatchers.IO) {
		val userFullName = user.getFullName()
		val successToastText =
			UiText.StringResource(R.string.toast_friend_deleted_successfully, userFullName)
		var uiEvent: UiEvent = UiEvent.ShowToast(successToastText)

		userRepository.deleteFriend(user.id)
			.onSuccessWithContext { response ->
				Log.d("ProfileViewModel", "deleted friend ${response.body}")
				_profileUiState.update {
					it.copy(
						isUserAFriendToAuthorizedUser = false,
						isRequestSent = false
					)
				}
			}
			.onFailureWithContext { error ->
				uiEvent = UiEvent.Error(error.errorBody, error.throwable)
			}
			.finallyWithContext { uiStateDispatcher.sendUiEvent(uiEvent) }
	}

	fun loadProfileOwner(id: UUID) = viewModelScope.launch(Dispatchers.Main) {
		val authorizedUser: User? = userDao.getCurrentUser()
		if (authorizedUser?.id == id)
			setProfileOwner(authorizedUser)
		else {
			val profileOwner: User? = getUserByIdUseCase(id)
			if (authorizedUser == null || profileOwner == null)
				return@launch
			setProfileOwner(profileOwner, authorizedUser)
		}
	}

	private fun setProfileOwner(profileOwner: User) = _profileUiState.update {
		it.copy(profileOwner = profileOwner)
	}

	private fun setProfileOwner(
		profileOwner: User,
		authorizedUser: User
	) {
		val isFriend: Boolean = authorizedUser.friends.any { it.id == profileOwner.id }

		_profileUiState.update { state ->
			state.copy(
				isUserAFriendToAuthorizedUser = isFriend,
				profileOwner = profileOwner
			)
		}
	}

	// post logic
	fun loadPostsByUser() = viewModelScope.launch(Dispatchers.IO) {
		val cache: List<Post> = postDao.getWallPosts()

		val (profileOwner: User?) = _profileUiState.value

		if (cache.hashCode() != _profileUiState.value.userPosts.hashCode() || _profileUiState.value.userPosts.isEmpty()) {
			_profileUiState.update { it.copy(postStatus = ApiStatus.LOADING) }
			profileOwner?.let {
				getPostsByUserUseCase(profileOwner)
					.onSuccess { response ->
						withContext(Dispatchers.Main) {
							postDao.updateWallPosts(response)

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
		} else {
			_profileUiState.update {
				it.copy(
					userPosts = cache,
					postStatus = ApiStatus.SUCCESS
				)
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