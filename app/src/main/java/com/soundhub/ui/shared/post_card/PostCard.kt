package com.soundhub.ui.shared.post_card

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.soundhub.data.states.UiState
import com.soundhub.domain.model.Post
import com.soundhub.domain.model.User
import com.soundhub.ui.theme.borderColor
import com.soundhub.ui.viewmodels.UiStateDispatcher
import java.util.UUID

@Composable
fun PostCard(
	modifier: Modifier = Modifier,
	navController: NavHostController,
	uiStateDispatcher: UiStateDispatcher,
	post: Post,
	onLikePost: (UUID) -> Unit = {},
	onDeletePost: (UUID) -> Unit = {}
) {
	val uiState: UiState by uiStateDispatcher.uiState.collectAsState(initial = UiState())
	val authorizedUser: User? = uiState.authorizedUser

	Card(
		colors = CardDefaults.cardColors(
			containerColor = MaterialTheme.colorScheme.surface,
			contentColor = MaterialTheme.colorScheme.onPrimaryContainer
		),
		modifier = modifier
			.fillMaxWidth()
			.shadow(
				elevation = 6.dp,
				spotColor = Color(0x40000000),
				ambientColor = Color(0x40000000)
			),
		border = BorderStroke(1.dp, borderColor)
	) {
		PostHeader(
			navController = navController,
			currentUser = authorizedUser,
			onDeletePost = onDeletePost,
			post = post
		)
		PostContent(textContent = post.content)
		PostImages(
			images = post.images,
			navController = navController,
			uiStateDispatcher = uiStateDispatcher
		)
		authorizedUser?.let {
			PostBottomPanel(
				post = post,
				uiStateDispatcher = uiStateDispatcher,
				onLikePost = onLikePost
			)
		}
	}
}