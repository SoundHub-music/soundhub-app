package com.soundhub.presentation.pages.postline

import android.content.Context
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.soundhub.R
import com.soundhub.data.enums.ApiStatus
import com.soundhub.domain.model.Post
import com.soundhub.domain.states.PostLineUiState
import com.soundhub.presentation.shared.containers.ContentContainer
import com.soundhub.presentation.shared.containers.FetchStatusContainer
import com.soundhub.presentation.shared.post_card.PostCard
import com.soundhub.presentation.viewmodels.UiStateDispatcher
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PostLineScreen(
	modifier: Modifier = Modifier,
	postLineViewModel: PostLineViewModel = hiltViewModel(),
	uiStateDispatcher: UiStateDispatcher,
	navController: NavHostController,
) {
	val context: Context = LocalContext.current
	val postLineUiState by postLineViewModel.postLineUiState.collectAsState(initial = PostLineUiState())

	val coroutineScope = rememberCoroutineScope()

	val refreshState = rememberPullToRefreshState()
	var isRefreshing: Boolean by remember { mutableStateOf(false) }

	val posts: List<Post> = postLineUiState.posts
	val fetchStatus: ApiStatus = postLineUiState.status

	var messageScreenText: String = rememberSaveable(posts) {
		context.getString(R.string.empty_postline_screen)
	}

	PullToRefreshBox(
		isRefreshing = isRefreshing,
		state = refreshState,
		onRefresh = {
			isRefreshing = true
			coroutineScope.launch {
				postLineViewModel.loadPosts()
				isRefreshing = false
			}
		}
	) {
		ContentContainer(
			contentAlignment = Alignment.Center,
			modifier = Modifier.padding(top = 10.dp)
		) {
			FetchStatusContainer(
				status = fetchStatus,
				isRefreshing = isRefreshing,
				handleLoading = !isRefreshing,
				onRefresh = { postLineViewModel.loadPosts() },
				emptyMessage = if (posts.isEmpty()) messageScreenText else null
			) {
				LazyColumn(
					modifier.fillMaxSize(),
					verticalArrangement = Arrangement.spacedBy(20.dp)
				) {
					items(items = posts, key = { it.id }) { post ->
						PostCard(
							navController = navController,
							post = post,
							uiStateDispatcher = uiStateDispatcher,
							onDeletePost = { postLineViewModel::deletePostById },
							onLikePost = { postLineViewModel::togglePostLikeAndUpdatePostList }
						)
					}
				}
			}
		}
	}
}