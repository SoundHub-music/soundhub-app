package com.soundhub.ui.postline

import android.content.Context
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.soundhub.R
import com.soundhub.data.enums.ApiStatus
import com.soundhub.data.model.User
import com.soundhub.ui.viewmodels.UiStateDispatcher
import com.soundhub.ui.components.loaders.CircleLoader
import com.soundhub.ui.components.containers.ContentContainer
import com.soundhub.ui.components.post_card.PostCard
import com.soundhub.ui.states.UiState
import com.soundhub.ui.viewmodels.PostViewModel

@Composable
fun PostLineScreen(
    modifier: Modifier = Modifier,
    postLineViewModel: PostLineViewModel = hiltViewModel(),
    postViewModel: PostViewModel = hiltViewModel(),
    uiStateDispatcher: UiStateDispatcher,
    navController: NavHostController,
) {
    val context: Context = LocalContext.current
    val postLineUiState by postLineViewModel.postLineUiState.collectAsState(initial = PostLineUiState())
    val uiState: UiState by uiStateDispatcher.uiState.collectAsState(initial = UiState())
    val authorizedUser: User? = uiState.authorizedUser

    val posts = postLineUiState.posts
    val fetchStatus: ApiStatus = postLineUiState.status

    var isError: Boolean by rememberSaveable { mutableStateOf(false) }
    var isLoading: Boolean by rememberSaveable { mutableStateOf(true) }
    var messageScreenText: String by rememberSaveable { mutableStateOf("") }

    LaunchedEffect(key1 = authorizedUser) {
        authorizedUser?.let { postLineViewModel.loadPosts() }
    }

    LaunchedEffect(key1 = fetchStatus) {
        isError = fetchStatus == ApiStatus.ERROR
        isLoading = fetchStatus == ApiStatus.LOADING

        messageScreenText = if (isError) context.getString(R.string.error_postline_screen)
        else if (posts.isEmpty()) context.getString(R.string.empty_postline_screen)
        else ""
    }

    ContentContainer(
        contentAlignment = Alignment.Center,
        modifier = Modifier.padding(top = 10.dp)
    ) {
        if (isLoading) CircleLoader(modifier = Modifier.size(72.dp))
        else if ((messageScreenText.isNotEmpty() && isError) || posts.isEmpty())
            Text(
                text = messageScreenText,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
        else LazyColumn(
                modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                items(items = posts, key = { it.id }) { post ->
                    PostCard(
                        navController = navController,
                        post = post,
                        uiStateDispatcher = uiStateDispatcher,
                        postViewModel = postViewModel,
                        onDeletePost = { id -> postLineViewModel.deletePostById(id) },
                        onLikePost = { id -> postLineViewModel.togglePostLikeAndUpdatePostList(id) }
                    )
                }
        }
    }
}