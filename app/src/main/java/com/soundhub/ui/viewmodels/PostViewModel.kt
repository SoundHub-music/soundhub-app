package com.soundhub.ui.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.soundhub.UiEvent
import com.soundhub.data.datastore.UserCredsStore
import com.soundhub.data.datastore.UserPreferences
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
    var postUiState: MutableStateFlow<PostUiState> = MutableStateFlow(PostUiState())
        private set

    fun isPostLiked(user: User, post: Post): Boolean {
        return postUiState.value.posts
            .any { it.likes.contains(user) && it == post }
    }

    fun getPostsByUser(user: User?) =
        viewModelScope.launch(Dispatchers.IO) {
            postUiState.update { it.copy(isLoading = true) }
            user?.let { user ->
                postRepository.getPostsByAuthorId(
                authorId = user.id,
                accessToken = userCreds.firstOrNull()?.accessToken
            )
            .onSuccess { response ->
                Log.d("AuthenticationViewModel", "getCurrentUserPosts[1]: ${response.body}")
                postUiState.update { it.copy(
                    posts = response.body?.sortedByDescending { p -> p.publishDate }
                        ?: emptyList()
                ) }
            }
            .finally {
                postUiState.update { it.copy(isLoading = false) }
            }
        }
    }

    suspend fun toggleLike(postId: UUID) {
        userCreds.collect { creds ->
            postRepository.toggleLike(
                accessToken = creds.accessToken,
                postId = postId
            )
            .onSuccess { response ->
                val updatedPost = response.body
                postUiState.update { state ->
                    // updating liked post in posts field
                    state.copy(
                        posts = state.posts.map {  post ->
                            updatedPost?.let {
                                if (post.id == it.id) {
                                    post.likes += updatedPost.likes
                                    post
                                }
                                else post
                            }
                            post
                        }
                    )
                }
            }
            .onFailure { response ->
                uiStateDispatcher.sendUiEvent(
                    UiEvent.ShowToast(UiText.DynamicString(response.errorBody.detail ?: ""))
                )
            }
        }
    }
}