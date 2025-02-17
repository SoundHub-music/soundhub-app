package com.soundhub.presentation.pages.music.pages.main.sections.friend_music

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.soundhub.R
import com.soundhub.domain.model.User
import com.soundhub.domain.states.UiState
import com.soundhub.presentation.pages.music.ui.cards.FriendMusicCard
import com.soundhub.presentation.viewmodels.UiStateDispatcher

@Composable
fun FriendMusicSection(uiStateDispatcher: UiStateDispatcher) {
	val uiState by uiStateDispatcher.uiState.collectAsState(initial = UiState())
	val authorizedUser: User? = uiState.authorizedUser
	val friends = authorizedUser?.friends.orEmpty()

	val lazyListState = rememberLazyListState()

	val isSingle = friends.size == 1

	Column(
		modifier = Modifier.fillMaxWidth()
	) {
		Text(
			text = stringResource(id = R.string.music_friend_music_section_title),
			fontWeight = FontWeight.ExtraBold,
			fontSize = 16.sp
		)
		LazyRow(
			state = lazyListState,
			modifier = Modifier.padding(top = 20.dp),
			horizontalArrangement = Arrangement.spacedBy(10.dp)
		) {
			items(items = friends, key = { it.id }) { friend ->
				if (friend.favoriteArtists.isEmpty()) return@items
				FriendMusicCard(friend, isSingle)
			}
		}
	}
}