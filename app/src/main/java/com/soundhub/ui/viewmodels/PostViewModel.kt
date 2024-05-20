package com.soundhub.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.soundhub.R
import com.soundhub.ui.events.UiEvent
import com.soundhub.data.datastore.UserCredsStore
import com.soundhub.data.datastore.UserPreferences
import com.soundhub.data.enums.ApiStatus
import com.soundhub.data.model.Post
import com.soundhub.data.model.User
import com.soundhub.data.repository.PostRepository
import com.soundhub.ui.states.PostUiState
import com.soundhub.utils.UiText
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class PostViewModel @Inject constructor(
    private val postRepository: PostRepository,
    private val uiStateDispatcher: UiStateDispatcher,
    userCredsStore: UserCredsStore
): ViewModel() {
    private val userCreds: Flow<UserPreferences> = userCredsStore.getCreds()
    val postUiState: MutableStateFlow<PostUiState> = MutableStateFlow(PostUiState())


    fun isPostLiked(user: User, post: Post): Boolean {
        return postUiState.value.posts
            .any { it.likes.contains(user) && it == post }
    }

    fun getPostsByUser(user: User) = viewModelScope.launch(Dispatchers.IO) {
        postUiState.update { it.copy(status = ApiStatus.LOADING) }

        postRepository.getPostsByAuthorId(
            authorId = user.id,
            accessToken = userCreds.firstOrNull()?.accessToken
        ).onSuccess { response ->
            val sortedPosts: List<Post> = response.body
                ?.sortedByDescending { p -> p.publishDate }
                .orEmpty()

            postUiState.update {
                it.copy(
                    posts = sortedPosts,
                    status = ApiStatus.SUCCESS
                )
            }
        }
        .onFailure {
            postUiState.update {
                it.copy(status = ApiStatus.ERROR)
            }
        }
    }

    fun deletePostById(id: UUID) = viewModelScope.launch(Dispatchers.IO) {
        val creds: UserPreferences? = userCreds.firstOrNull()
        postRepository.deletePost(
            accessToken = creds?.accessToken,
            postId = id
        )
            .onSuccess {
                postUiState.update { state ->
                    state.copy(posts = state.posts.filter { post -> post.id != id })
                }
                uiStateDispatcher.sendUiEvent(
                    UiEvent.ShowToast(UiText
                        .StringResource(R.string.toast_post_deleted_successfully))
                )
            }
            .onFailure {
                uiStateDispatcher
                    .sendUiEvent(
                        UiEvent.ShowToast(UiText
                            .StringResource(R.string.toast_delete_post_error_message))
                    )
            }
    }

    fun toggleLike(postId: UUID) = viewModelScope.launch(Dispatchers.IO) {
        val creds: UserPreferences? = userCreds.firstOrNull()

        postRepository.toggleLike(
            accessToken = creds?.accessToken,
            postId = postId
        )
        .onSuccess { response -> updatePostInList(response.body) }
        .onFailure { error ->
            val toastText: UiText.DynamicString = UiText.DynamicString(error.errorBody.detail ?: "")
            uiStateDispatcher.sendUiEvent(UiEvent.ShowToast(toastText))
        }
    }

    private fun updatePostInList(updatedPost: Post?) {
        postUiState.update { state ->
            val updatedPostList: List<Post> = state.posts.map { post ->
                updatedPost?.let {
                    if (post.id == it.id) {
                        post.likes += updatedPost.likes
                        post
                    }
                    else post
                }
                post
            }
            // updating liked post in posts field
            state.copy(posts = updatedPostList)
        }
    }
}