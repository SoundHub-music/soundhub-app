package com.soundhub.ui.postline

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.soundhub.data.dao.UserDao
import com.soundhub.data.datastore.UserCredsStore
import com.soundhub.data.datastore.UserPreferences
import com.soundhub.data.enums.ApiStatus
import com.soundhub.data.model.Post
import com.soundhub.data.model.User
import com.soundhub.data.repository.PostRepository
import com.soundhub.domain.usecases.UseCaseResult
import com.soundhub.domain.usecases.post.DeletePostByIdUseCase
import com.soundhub.domain.usecases.post.TogglePostLikeAndUpdateListUseCase
import com.soundhub.ui.events.UiEvent
import com.soundhub.ui.viewmodels.UiStateDispatcher
import com.soundhub.utils.UiText
import com.soundhub.utils.constants.Constants.UNAUTHORIZED_USER_ERROR_CODE
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class PostLineViewModel @Inject constructor(
    private val userDao: UserDao,
    private val postRepository: PostRepository,
    private val uiStateDispatcher: UiStateDispatcher,
    private val deletePostByIdUseCase: DeletePostByIdUseCase,
    private val togglePostLikeAndUpdateListUseCase: TogglePostLikeAndUpdateListUseCase,
    userCredsStore: UserCredsStore
) : ViewModel() {
    private val userCreds: Flow<UserPreferences> = userCredsStore.getCreds()
    private val loadPostsAttemptCount: MutableStateFlow<Int> = MutableStateFlow(0)

    private val _postLineUiState: MutableStateFlow<PostLineUiState> = MutableStateFlow(PostLineUiState())
    val postLineUiState: Flow<PostLineUiState> = _postLineUiState.asStateFlow()

    init { loadPosts() }

    private fun loadPosts() = viewModelScope.launch(Dispatchers.IO) {
        val authorizedUser: User? = userDao.getCurrentUser()
        val friends: List<User> = authorizedUser?.friends.orEmpty()

        friends.forEach { user -> fetchAndProcessPosts(user) }
    }

    private suspend fun fetchAndProcessPosts(user: User) {
        val creds: UserPreferences? = userCreds.firstOrNull()

        postRepository.getPostsByAuthorId(
            accessToken = creds?.accessToken,
            authorId = user.id
        ).onSuccess { response ->
            _postLineUiState.update { state ->
                state.copy(
                    posts = (state.posts + response.body.orEmpty()).sortedBy { it.publishDate },
                    status = ApiStatus.SUCCESS
                )
            }
        }.onFailure { error ->
            loadPostsAttemptCount.update { loadPostsAttemptCount.value++ }
            if (error.errorBody.status == UNAUTHORIZED_USER_ERROR_CODE && loadPostsAttemptCount.value > 2) {
                loadPosts()
            }
            loadPostsAttemptCount.update { 0 }

            val errorEvent: UiEvent = UiEvent.Error(error.errorBody, error.throwable)
            uiStateDispatcher.sendUiEvent(errorEvent)
            _postLineUiState.update { it.copy(status = ApiStatus.ERROR) }
        }
    }


    fun deletePostById(postId: UUID) = viewModelScope.launch(Dispatchers.IO) {
        when (val result = deletePostByIdUseCase(postId)) {
            is UseCaseResult.Success -> _postLineUiState.update { state ->
                val deletedPostId: UUID? = result.data
                state.copy(
                    posts = state.posts.filter { deletedPostId != it.id },
                    status = ApiStatus.SUCCESS
                )
            }
            else -> _postLineUiState.update { it.copy(status = ApiStatus.ERROR) }
        }
    }

    fun togglePostLikeAndUpdatePostList(postId: UUID) = viewModelScope.launch(Dispatchers.IO) {
        val ( posts: List<Post> ) = _postLineUiState.value

        when (val result = togglePostLikeAndUpdateListUseCase(postId, posts)) {
            is UseCaseResult.Success -> _postLineUiState.update {
                val updatedPostList: List<Post> = result.data.orEmpty()
                it.copy(posts = updatedPostList)
            }
            is UseCaseResult.Failure -> {
                val toastText: UiText = UiText.DynamicString(result.error?.detail ?: "")
                uiStateDispatcher.sendUiEvent(UiEvent.ShowToast(toastText))
            }
        }
    }
}