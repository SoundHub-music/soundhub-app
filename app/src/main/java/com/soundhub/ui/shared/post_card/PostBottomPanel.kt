package com.soundhub.ui.shared.post_card

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.soundhub.R
import com.soundhub.data.states.UiState
import com.soundhub.domain.model.Post
import com.soundhub.domain.model.User
import com.soundhub.ui.shared.buttons.LikeButton
import com.soundhub.ui.viewmodels.UiStateDispatcher
import java.util.UUID

@Composable
internal fun PostBottomPanel(
	post: Post,
	uiStateDispatcher: UiStateDispatcher,
	onLikePost: (UUID) -> Unit
) {
	val uiState: UiState by uiStateDispatcher.uiState.collectAsState(initial = UiState())
	val authorizedUser: User? = uiState.authorizedUser
	var isFavorite: Boolean by rememberSaveable {
		mutableStateOf(false)
	}

	LaunchedEffect(key1 = post, key2 = authorizedUser) {
		isFavorite = post.isPostLikedByUser(authorizedUser, post)
	}

	Row(
		modifier = Modifier
			.fillMaxWidth()
			.padding(8.dp),
		horizontalArrangement = Arrangement.End,
		verticalAlignment = Alignment.CenterVertically
	) {
		if (post.likes.isNotEmpty())
			Text(
				text = post.likes.size.toString(),
				fontWeight = FontWeight.Bold,
				fontFamily = FontFamily(Font(R.font.nunito_bold_italic))
			)

		LikeButton(isFavorite = isFavorite) {
			isFavorite = !isFavorite

			authorizedUser?.let {
				if (isFavorite)
					post.likes += authorizedUser
				else post.likes = post.likes
					.filter { it.id != authorizedUser.id }
					.toSet()
			}
			onLikePost(post.id)
		}
	}
}