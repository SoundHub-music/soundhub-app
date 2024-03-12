package com.soundhub.ui.postline

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.soundhub.data.model.Post
import com.soundhub.data.model.User
import com.soundhub.ui.states.PostlineUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.Month
import javax.inject.Inject

@HiltViewModel
class PostlineViewModel @Inject constructor() : ViewModel() {
    var postsUiState: MutableStateFlow<PostlineUiState> =
        MutableStateFlow(PostlineUiState(isLoading = true))
        private set

    init {
        // TODO: implement post fetching
        viewModelScope.launch {
            val posts = listOf(
                Post(
                    author = User(firstName = "Billie", lastName = "Elish"),
                    publishDate = LocalDateTime.now(),
                    content = "sdfksofsopgsrgk",
                    avatar = null,
                    imageContent = listOf(
                        "https://images.unsplash.com/photo-1503023345310-bd7c1de61c7d?q=80&w=1000&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxzZWFyY2h8Mnx8aHVtYW58ZW58MHx8MHx8fDA%3D",
                        "https://media.istockphoto.com/id/1322277517/photo/wild-grass-in-the-mountains-at-sunset.jpg?s=612x612&w=0&k=20&c=6mItwwFFGqKNKEAzv0mv6TaxhLN3zSE43bWmFN--J5w="
                    )
                ),
                Post(
                    author = User(firstName = "Billy", lastName = "Elish"),
                    publishDate = LocalDateTime.of(2024, Month.FEBRUARY, 24, 12, 11),
                    content = "sdfksofsopgsrgk",
                    avatar = null,
                    imageContent = listOf("https://img.freepik.com/free-photo/painting-mountain-lake-with-mountain-background_188544-9126.jpg")
                ),
                Post(
                    author = User(firstName = "Billy", lastName = "Elish"),
                    publishDate = LocalDateTime.of(2024, Month.FEBRUARY, 26, 12, 11),
                    content = "sdfksofsopgsrgk",
                    avatar = null,
                    imageContent = emptyList()
                ),
                Post(
                    author = User(firstName = "Billy", lastName = "Elish"),
                    publishDate = LocalDateTime.of(2024, Month.FEBRUARY, 24, 12, 11),
                    content = "sdfksofsopgsrgk",
                    avatar = null,
                    imageContent = emptyList()
                ),
                Post(
                    author = User(firstName = "Billy", lastName = "Elish"),
                    publishDate = LocalDateTime.of(2024, Month.FEBRUARY, 24, 12, 11),
                    content = "sdfksofsopgsrgk",
                    avatar = null,
                    imageContent = emptyList()
                ),
                Post(
                    author = User(firstName = "Billy", lastName = "Elish"),
                    publishDate = LocalDateTime.of(2024, Month.FEBRUARY, 24, 12, 11),
                    content = "sdfksofsopgsrgk",
                    avatar = null,
                    imageContent = emptyList()
                )

            )
            // fetching posts imitation
            delay(2000)
            postsUiState.update { it.copy(isLoading = false, posts = posts) }
        }

    }
}