package com.soundhub.ui.postline

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.soundhub.data.dao.UserDao
import com.soundhub.data.datastore.UserCredsStore
import com.soundhub.data.datastore.UserPreferences
import com.soundhub.data.enums.ApiStatus
import com.soundhub.data.model.User
import com.soundhub.data.repository.PostRepository
import com.soundhub.ui.states.PostlineUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PostlineViewModel @Inject constructor(
    private val userDao: UserDao,
    private val postRepository: PostRepository,
    userCredsStore: UserCredsStore
) : ViewModel() {
    private val userCreds: Flow<UserPreferences> = userCredsStore.getCreds()
    private val _postLineUiState: MutableStateFlow<PostlineUiState> =
        MutableStateFlow(PostlineUiState())

    val postLineUiState: Flow<PostlineUiState> = _postLineUiState.asStateFlow()

    init { loadPosts() }

    private fun loadPosts() = viewModelScope.launch(Dispatchers.IO) {
        val authorizedUser: User? = userDao.getCurrentUser()
        val friends: List<User> = authorizedUser?.friends.orEmpty()

        friends.forEach { user ->
            postRepository.getPostsByAuthorId(
                accessToken = userCreds.firstOrNull()?.accessToken,
                authorId = user.id
            )
            .onSuccess { response ->
                _postLineUiState.update { state ->
                    state.copy(posts = (state.posts + response.body.orEmpty()).sortedBy { it.publishDate })
                }
            }
        }

        _postLineUiState.update { it.copy(status = ApiStatus.SUCCESS) }
    }
}