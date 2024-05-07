package com.soundhub.ui.post_editor

import androidx.lifecycle.ViewModel
import com.soundhub.R
import com.soundhub.ui.events.UiEvent
import com.soundhub.data.datastore.UserCredsStore
import com.soundhub.data.datastore.UserPreferences
import com.soundhub.data.model.Post
import com.soundhub.data.model.User
import com.soundhub.data.repository.PostRepository
import com.soundhub.ui.viewmodels.UiStateDispatcher
import com.soundhub.utils.UiText
import com.soundhub.utils.mappers.PostMapper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.firstOrNull
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
    val postEditorState: MutableStateFlow<PostEditorState> = MutableStateFlow(PostEditorState())

    fun setContent(value: String) = postEditorState.update {
        it.copy(content = value)
    }

    suspend fun loadPost(postId: UUID) {
        val creds: UserPreferences? = userCreds.firstOrNull()

        postRepository.getPostById(
            accessToken = creds?.accessToken,
            postId = postId
        ).onSuccess { response ->
            val post: Post? = response.body
            post?.let {
                val newState: PostEditorState = PostMapper.impl
                    .fromPostToPostEditorState(post)

                postEditorState.update { newState }
            }
        }
    }

    suspend fun createPost(author: User?) {
        val creds: UserPreferences? = userCreds.firstOrNull()
        var toastText: UiText = UiText.StringResource(R.string.toast_post_created_successfully)
        val post = Post(
            author = author,
            content = postEditorState.value.content
        )

        postRepository.addPost(
            post = post,
            accessToken = creds?.accessToken
        ).onSuccess {
            uiStateDispatcher.sendUiEvent(UiEvent.ShowToast(toastText))
            uiStateDispatcher.sendUiEvent(UiEvent.PopBackStack)
        }
        .onFailure { response ->
            response.errorBody.detail?.let { message ->
                toastText = UiText.DynamicString(message)
                uiStateDispatcher.sendUiEvent(UiEvent.ShowToast(toastText))
            }
        }
    }

    suspend fun updatePost(imagesToBeDeleted: List<String> = emptyList()) {
        val creds: UserPreferences? = userCreds.firstOrNull()
        val post: Post = PostMapper.impl.fromPostEditorStateToPost(postEditorState.value)
        var toastText: UiText

        postRepository.updatePost(
            accessToken = creds?.accessToken,
            postId = post.id,
            post = post,
            imagesToBeDeleted = imagesToBeDeleted
        )
            .onSuccess {
                toastText = UiText.StringResource(R.string.toast_post_updated_successfully)
                uiStateDispatcher.sendUiEvent(UiEvent.ShowToast(toastText))
            }
            .onFailure {
                toastText = UiText.StringResource(R.string.toast_update_post_error)
                uiStateDispatcher.sendUiEvent(UiEvent.ShowToast(toastText))
            }
    }
}