package com.soundhub.ui.create_post

import androidx.lifecycle.ViewModel
import com.soundhub.R
import com.soundhub.UiEvent
import com.soundhub.data.datastore.UserCredsStore
import com.soundhub.data.model.Post
import com.soundhub.data.model.User
import com.soundhub.data.repository.PostRepository
import com.soundhub.ui.viewmodels.UiStateDispatcher
import com.soundhub.utils.UiText
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class CreatePostViewModel @Inject constructor(
    private val postRepository: PostRepository,
    private val uiStateDispatcher: UiStateDispatcher,
    userCredsStore: UserCredsStore
): ViewModel() {
    private val userCreds = userCredsStore.getCreds()
    var postState: MutableStateFlow<PostState> = MutableStateFlow(PostState())
        private set

    fun setContent(value: String) = postState.update {
        it.copy(content = value)
    }

    suspend fun createPost(author: User?) {
        val post = Post(
            author = author,
            content = postState.value.content,
        )
        userCreds.collect { creds ->
            postRepository.addPost(
                post = post,
                accessToken = creds.accessToken
            )
            .onSuccess {
                uiStateDispatcher.sendUiEvent(
                    UiEvent.ShowToast(UiText.StringResource(R.string.post_created_successfully))
                )
                uiStateDispatcher.sendUiEvent(UiEvent.PopBackStack)
            }
            .onFailure { response ->
                response.errorBody.detail?.let { message ->
                    uiStateDispatcher.sendUiEvent(UiEvent.ShowToast(
                        UiText.DynamicString(message)
                    ))
                }
            }
        }

    }
}