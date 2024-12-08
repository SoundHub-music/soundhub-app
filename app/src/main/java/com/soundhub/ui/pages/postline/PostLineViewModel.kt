package com.soundhub.ui.pages.postline

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.soundhub.data.enums.ApiStatus
import com.soundhub.data.local_database.dao.UserDao
import com.soundhub.domain.events.UiEvent
import com.soundhub.domain.model.Post
import com.soundhub.domain.model.User
import com.soundhub.domain.repository.PostRepository
import com.soundhub.domain.usecases.post.DeletePostByIdUseCase
import com.soundhub.domain.usecases.post.TogglePostLikeAndUpdateListUseCase
import com.soundhub.ui.viewmodels.UiStateDispatcher
import com.soundhub.utils.lib.UiText
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class PostLineViewModel @Inject constructor(
	private val userDao: UserDao,
	private val postRepository: PostRepository,
	private val uiStateDispatcher: UiStateDispatcher,
	private val deletePostByIdUseCase: DeletePostByIdUseCase,
	private val togglePostLikeAndUpdateListUseCase: TogglePostLikeAndUpdateListUseCase,
) : ViewModel() {
	private val _postLineUiState: MutableStateFlow<PostLineUiState> = MutableStateFlow(
		PostLineUiState()
	)
	val postLineUiState: Flow<PostLineUiState> = _postLineUiState.asStateFlow()

	fun loadPosts() = viewModelScope.launch(Dispatchers.IO) {
		_postLineUiState.update { it.copy(status = ApiStatus.LOADING) }

		val authorizedUser: User? = userDao.getCurrentUser()
		val friends: List<User> = authorizedUser?.friends.orEmpty()

		friends.forEach { user -> fetchAndProcessPosts(user) }.also {
			if (friends.isEmpty())
				_postLineUiState.update { it.copy(status = ApiStatus.SUCCESS) }
		}
	}

	private suspend fun fetchAndProcessPosts(user: User) {
		var posts: MutableList<Post> = _postLineUiState.value.posts.toMutableList()

		postRepository.getPostsByAuthorId(user.id)
			.onSuccessWithContext { response ->
				val postsFromResponse: List<Post> = response.body.orEmpty()
				if (posts.isNotEmpty())
					posts.addAll(postsFromResponse.filter { p -> p.id !in posts.map { it.id } })
				else posts = postsFromResponse.toMutableList()

				_postLineUiState.update { state ->
					state.copy(
						posts = posts.sortedBy { it.createdAt },
						status = ApiStatus.SUCCESS
					)
				}
			}.onFailure { error ->
				val errorEvent: UiEvent = UiEvent.Error(error.errorBody, error.throwable)
				uiStateDispatcher.sendUiEvent(errorEvent)
				_postLineUiState.update { it.copy(status = ApiStatus.ERROR) }
			}
	}


	fun deletePostById(postId: UUID) = viewModelScope.launch(Dispatchers.IO) {
		deletePostByIdUseCase(postId)
			.onSuccess { response ->
				withContext(Dispatchers.Main) {
					_postLineUiState.update { state ->
						val deletedPostId: UUID? = response
						state.copy(
							posts = state.posts.filter { deletedPostId != it.id },
							status = ApiStatus.SUCCESS
						)
					}
				}
			}
			.onFailure {
				withContext(Dispatchers.Main) {
					_postLineUiState.update { state -> state.copy(status = ApiStatus.ERROR) }
				}
			}
	}

	fun togglePostLikeAndUpdatePostList(postId: UUID) = viewModelScope.launch(Dispatchers.IO) {
		val (posts: List<Post>) = _postLineUiState.value
		togglePostLikeAndUpdateListUseCase(postId, posts)
			.onSuccess { response ->
				withContext(Dispatchers.Main) {
					_postLineUiState.update {
						val updatedPostList: List<Post> = response
						it.copy(posts = updatedPostList)
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