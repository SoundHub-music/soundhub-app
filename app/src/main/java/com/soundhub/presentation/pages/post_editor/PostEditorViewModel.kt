package com.soundhub.presentation.pages.post_editor

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.soundhub.R
import com.soundhub.domain.events.UiEvent
import com.soundhub.domain.model.Post
import com.soundhub.domain.model.User
import com.soundhub.domain.repository.PostRepository
import com.soundhub.domain.states.PostEditorState
import com.soundhub.presentation.viewmodels.UiStateDispatcher
import com.soundhub.utils.lib.UiText
import com.soundhub.utils.mappers.PostMapper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class PostEditorViewModel @Inject constructor(
	private val postRepository: PostRepository,
	private val uiStateDispatcher: UiStateDispatcher,
) : ViewModel() {
	private val _postEditorState = MutableStateFlow(PostEditorState())
	val postEditorState = _postEditorState.asStateFlow()

	fun setContent(value: String) = _postEditorState.update {
		it.copy(content = value)
	}

	suspend fun loadPost(postId: UUID) {
		postRepository.getPostById(postId)
			.onSuccessWithContext { response ->
				val post: Post? = response.body
				post?.let {
					val newState: PostEditorState = PostMapper.impl
						.fromPostToPostEditorState(post)
						.apply {
							this.doesPostExist = true
							this.oldPostState = post
						}

					_postEditorState.update { newState }
				}
			}
	}

	fun setImages(list: List<String>) {
		val doesPostExist: Boolean = _postEditorState.value.doesPostExist

		if (doesPostExist)
			_postEditorState.update { it.copy(newImages = list) }
		else {
			var images: MutableList<String> = _postEditorState.value.images.toMutableList()
			if (images.isNotEmpty())
				images += list
			else images = list.toMutableList()

			_postEditorState.update { it.copy(images = images) }
		}
	}

	fun onSavePostClick(postId: UUID?, author: User?) = viewModelScope.launch {
		postId?.let { updatePost() }
			?: run { createPost(author) }
	}

	fun deleteImage(uri: String) = _postEditorState.update {
		val doesPostExist: Boolean = _postEditorState.value.doesPostExist
		val originalImages: List<String> = _postEditorState.value.oldPostState?.images.orEmpty()
		val imagesToBeDeleted: MutableList<String> = it.imagesToBeDeleted.toMutableList()

		if (doesPostExist && uri in originalImages) {
			imagesToBeDeleted += uri
		}

		it.copy(
			images = it.images.filter { u -> u != uri },
			imagesToBeDeleted = it.imagesToBeDeleted + imagesToBeDeleted
		)
	}

	private suspend fun createPost(author: User?) {
		var toastText: UiText = UiText.StringResource(R.string.toast_post_created_successfully)
		val post: Post = PostMapper.impl.fromPostEditorStateToPost(_postEditorState.value)
			.apply { this.author = author }

		postRepository.addPost(post)
			.onSuccessWithContext {
				with(uiStateDispatcher) {
					sendUiEvent(UiEvent.ShowToast(toastText))
					sendUiEvent(UiEvent.PopBackStack)
				}
			}.onFailureWithContext { response ->
				response.errorBody.detail?.let { message ->
					toastText = UiText.DynamicString(message)
					uiStateDispatcher.sendUiEvent(UiEvent.ShowToast(toastText))
				}
			}
	}

	private suspend fun updatePost() {
		val post: Post = PostMapper.impl.fromPostEditorStateToPost(_postEditorState.value)
		var toastText: UiText

		postRepository.updatePost(
			postId = post.id,
			post = post,
			newImages = _postEditorState.value.newImages,
			imagesToBeDeleted = _postEditorState.value.imagesToBeDeleted
		)
			.onSuccessWithContext {
				toastText = UiText.StringResource(R.string.toast_post_updated_successfully)
				with(uiStateDispatcher) {
					sendUiEvent(UiEvent.ShowToast(toastText))
					sendUiEvent(UiEvent.PopBackStack)
				}
			}
			.onFailureWithContext {
				toastText = UiText.StringResource(R.string.toast_update_post_error)
				uiStateDispatcher.sendUiEvent(UiEvent.ShowToast(toastText))
			}
	}
}