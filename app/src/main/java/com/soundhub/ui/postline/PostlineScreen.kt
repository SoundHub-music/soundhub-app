package com.soundhub.ui.postline

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.soundhub.R
import com.soundhub.data.model.User
import com.soundhub.ui.viewmodels.UiStateDispatcher
import com.soundhub.ui.components.loaders.CircleLoader
import com.soundhub.ui.components.containers.ContentContainer
import com.soundhub.ui.components.post_card.PostCard
import com.soundhub.ui.viewmodels.PostViewModel

@Composable
fun PostLineScreen(
    modifier: Modifier = Modifier,
    postViewModel: PostViewModel = hiltViewModel(),
    uiStateDispatcher: UiStateDispatcher,
    navController: NavHostController,
    currentUser: User?
) {
    val postUiState by postViewModel.postUiState.collectAsState()

    ContentContainer(contentAlignment = Alignment.Center) {
        if (postUiState.isLoading) CircleLoader(modifier = Modifier.size(72.dp))
        else if (postUiState.posts.isEmpty())
            Text(
                text = stringResource(id = R.string.empty_postline_screen),
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
        else {
            LazyColumn(
                modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                items(items = postUiState.posts, key = { it.id }) { post ->
                    PostCard(
                        navController = navController,
                        post = post,
                        uiStateDispatcher = uiStateDispatcher,
                        currentUser = currentUser,
                        postViewModel = postViewModel
                    )
                }
            }
        }
    }
}