package com.soundhub.presentation.pages.profile.ui.sections.wall

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.soundhub.R
import com.soundhub.data.enums.ApiStatus
import com.soundhub.domain.model.Post
import com.soundhub.domain.states.ProfileUiState
import com.soundhub.presentation.pages.profile.ProfileViewModel
import com.soundhub.presentation.pages.profile.ui.SectionLabel
import com.soundhub.presentation.shared.containers.FetchStatusContainer
import com.soundhub.presentation.shared.post_card.PostCard
import com.soundhub.presentation.viewmodels.UiStateDispatcher

@Composable
internal fun UserWall(
	navController: NavHostController,
	uiStateDispatcher: UiStateDispatcher,
	profileViewModel: ProfileViewModel,
) {
	val profileUiState: ProfileUiState by profileViewModel.profileUiState.collectAsState()
	val posts: List<Post> = profileUiState.userPosts
	val fetchStatus: ApiStatus = profileUiState.postStatus

	Column(
		modifier = Modifier
			.fillMaxSize()
			.padding(vertical = 10.dp),
		verticalArrangement = Arrangement.spacedBy(10.dp),
	) {
		SectionLabel(
			text = stringResource(id = R.string.profile_screen_user_posts),
			labelIcon = painterResource(id = R.drawable.round_sticky_note_2_24),
			iconTint = Color(0xFFFFC107),
		)

		FetchStatusContainer(
			status = fetchStatus,
			modifier = Modifier.padding(top = 20.dp)
		) {
			posts.forEach { post ->
				PostCard(
					post = post,
					navController = navController,
					uiStateDispatcher = uiStateDispatcher,
					onDeletePost = { id -> profileViewModel.deletePostById(id) },
					onLikePost = { id -> profileViewModel.togglePostLikeAndUpdatePostList(id) }
				)
			}
		}
	}
}