package com.soundhub.presentation.pages.friends

import android.util.Log
import androidx.compose.foundation.pager.PagerState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.soundhub.Route
import com.soundhub.data.enums.ApiStatus
import com.soundhub.data.local_database.dao.UserDao
import com.soundhub.domain.events.UiEvent
import com.soundhub.domain.model.Chat
import com.soundhub.domain.model.User
import com.soundhub.domain.repository.UserRepository
import com.soundhub.domain.states.FriendsUiState
import com.soundhub.domain.usecases.chat.GetOrCreateChatByUserUseCase
import com.soundhub.domain.usecases.user.GetUserByIdUseCase
import com.soundhub.presentation.pages.friends.enums.FriendListPage
import com.soundhub.presentation.viewmodels.UiStateDispatcher
import com.soundhub.utils.lib.SearchUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

@OptIn(FlowPreview::class)
@HiltViewModel
class FriendsViewModel @Inject constructor(
	private val userRepository: UserRepository,
	private val uiStateDispatcher: UiStateDispatcher,
	private val getOrCreateChatByUserUseCase: GetOrCreateChatByUserUseCase,
	private val getUserByIdUseCase: GetUserByIdUseCase,
	private val userDao: UserDao,
) : ViewModel() {
	private val authorizedUser = MutableStateFlow<User?>(null)

	private val _friendsUiState = MutableStateFlow(FriendsUiState())
	val friendsUiState: StateFlow<FriendsUiState> = _friendsUiState.asStateFlow()

	private val tabs: List<FriendListPage> = listOf(
		FriendListPage.MAIN,
		FriendListPage.RECOMMENDATIONS,
		FriendListPage.SEARCH
	)

	private val _currentTabIndex = MutableStateFlow(0)
	val currentTabIndex = _currentTabIndex.asStateFlow()

	init {
		viewModelScope.launch {
			initializeFriendsUiState()

			uiStateDispatcher.uiState.map { it.searchBarText }
				.debounce(500)
				.distinctUntilChanged()
				.collect {
					if (tabs[currentTabIndex.value] == FriendListPage.SEARCH) {
						searchUsers(it)
					}
				}
		}
	}

	override fun onCleared() {
		super.onCleared()
		Log.d("FriendsViewModel", "onCleared: clear vm state")
		_friendsUiState.update { FriendsUiState() }
	}

	private suspend fun initializeFriendsUiState() = authorizedUser.update {
		userDao.getCurrentUser()
	}

	fun getTabs() = tabs

	fun setCurrentTabIndex(index: Int) = _currentTabIndex.update { index }

	fun onNavigateToChatBtnClick(user: User) = viewModelScope.launch(Dispatchers.Main) {
		getOrCreateChat(user)
			.firstOrNull()
			?.let {
				val route: Route = Route.Messenger.Chat
					.getRouteWithNavArg(it.id.toString())

				uiStateDispatcher.sendUiEvent(UiEvent.Navigate(route))
			}
	}

	fun loadRecommendedFriends() = viewModelScope.launch(Dispatchers.IO) {
		_friendsUiState.update { it.copy(status = ApiStatus.LOADING) }
		val cachedRecommendedUsers = _friendsUiState.value.recommendedFriends

		// caching recommended users in viewmodel scope
		if (cachedRecommendedUsers.isNotEmpty()) {
			_friendsUiState.update {
				it.copy(
					recommendedFriends = cachedRecommendedUsers,
					status = ApiStatus.SUCCESS
				)
			}

			return@launch
		}

		userRepository.getRecommendedFriends()
			.onSuccess { response ->
				val users: List<User> = response.body.orEmpty()
				_friendsUiState.update {
					it.copy(
						recommendedFriends = users,
						status = ApiStatus.SUCCESS
					)
				}
			}.onFailure { error ->
				val errorEvent: UiEvent = UiEvent.Error(error.errorBody, error.throwable)
				uiStateDispatcher.sendUiEvent(errorEvent)
				_friendsUiState.update { it.copy(status = ApiStatus.ERROR) }
			}
	}

	private fun getOrCreateChat(interlocutor: User): Flow<Chat?> = flow {
		authorizedUser.value?.let { user ->
			val chat: Chat? = getOrCreateChatByUserUseCase(
				interlocutor = interlocutor,
				userId = user.id
			)
			emit(chat)

		} ?: emit(null)
	}

	suspend fun filterFriendsList(occurrenceString: String, users: List<User>): List<User> {
		if (occurrenceString.isEmpty() || occurrenceString.isBlank())
			return users

		delay(500)
		return users.filter { SearchUtils.compareWithUsername(it, occurrenceString) }
	}

	fun searchUsers(username: String) {
		if (username.isEmpty() || username.isBlank()) {
			_friendsUiState.update { it.copy(foundUsers = emptyList()) }
			return
		}

		viewModelScope.launch(Dispatchers.IO) {
			userRepository.searchUserByFullName(username)
				.onSuccess { response ->
					val otherUsers: List<User> = response.body
						?.filter { user -> user.id != authorizedUser.firstOrNull()?.id }
						.orEmpty()

					_friendsUiState.update {
						it.copy(
							foundUsers = otherUsers,
							status = ApiStatus.SUCCESS
						)
					}
				}
				.onFailure { error ->
					val errorEvent: UiEvent = UiEvent.Error(error.errorBody, error.throwable)
					uiStateDispatcher.sendUiEvent(errorEvent)
					_friendsUiState.update {
						it.copy(
							status = ApiStatus.ERROR,
							foundUsers = emptyList()
						)
					}
				}
		}
	}

	private fun setProfileOwner(user: User?) = viewModelScope.launch(Dispatchers.IO) {
		_friendsUiState.update {
			it.copy(profileOwner = user)
		}
	}

	fun loadProfileOwner(id: UUID) = viewModelScope.launch(Dispatchers.IO) {
		val profileOwner: User? = if (authorizedUser.value?.id == id)
			authorizedUser.value
		else getUserByIdUseCase(id)

		setProfileOwner(profileOwner)
	}

	fun isOriginProfile(): Boolean {
		val authorizedUser: User? = authorizedUser.value
		val profileOwner: User? = _friendsUiState.value.profileOwner
		return authorizedUser?.id == profileOwner?.id
	}

	fun loadUsersCompatibility(userIds: List<UUID>) = viewModelScope.launch(Dispatchers.IO) {
		userRepository.getUsersCompatibilityPercentage(userIds)
			.onSuccess { response ->
				_friendsUiState.update {
					it.copy(
						userCompatibilityPercentage = response.body
					)
				}
			}
			.onFailure { error ->
				val uiEvent: UiEvent = UiEvent.Error(error.errorBody, error.throwable)
				uiStateDispatcher.sendUiEvent(uiEvent)
			}
	}

	fun handleSearchFriendClick(pagerState: PagerState) = viewModelScope.launch {
		pagerState.animateScrollToPage(tabs.indexOf(FriendListPage.RECOMMENDATIONS))
	}
}