package com.soundhub.ui.post_editor

import android.util.Log
import androidx.lifecycle.ViewModel
import com.soundhub.R
import com.soundhub.ui.events.UiEvent
import com.soundhub.data.datastore.UserCredsStore
import com.soundhub.data.model.Post
import com.soundhub.data.model.User
import com.soundhub.data.repository.PostRepository
import com.soundhub.ui.viewmodels.UiStateDispatcher
import com.soundhub.utils.UiText
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class PostEditorViewModel @Inject constructor(
    private val postRepository: PostRepository,
    private val uiStateDispatcher: UiStateDispatcher,
    userCredsStore: UserCredsStore
): ViewModel() {
    private val userCreds = userCredsStore.getCreds()
    var postEditorState: MutableStateFlow<PostEditorState> = MutableStateFlow(PostEditorState())
        private set

    fun setContent(value: String) = postEditorState.update {
        it.copy(content = value)
    }

    suspend fun loadPost(postId: UUID) = userCreds.collect { creds ->
        postRepository.getPostById(
            accessToken = creds.accessToken,
            postId = postId
        )
            .onSuccess { response ->
                val post: Post? = response.body
                Log.d("PostEditorViewModel", "loadPost: $response")
                postEditorState.update { it.copy(
                    content = post?.content ?: "",
                    images = post?.images ?: emptyList(),
                    author = post?.author
                ) }
            }
            .onFailure {
                Log.e("PostEditorViewModel", "loadPost: ${it.errorBody}")
            }
    }

    suspend fun createPost(author: User?) {
        val post = Post(
            author = author,
            content = postEditorState.value.content,
        )
        userCreds.collect { creds ->
            postRepository.addPost(
                post = post,
                accessToken = creds.accessToken
            )
            .onSuccess {
                uiStateDispatcher.sendUiEvent(
                    UiEvent.ShowToast(UiText.StringResource(R.string.toast_post_created_successfully))
                )
                uiStateDispatcher.sendUiEvent(UiEvent.PopBackStack)
            }
            .onFailure { response ->
                response.errorBody.detail?.let { message ->
                    uiStateDispatcher.sendUiEvent(
                        UiEvent.ShowToast(
                        UiText.DynamicString(message)
                    ))
                }
            }
        }
    }
}