package com.soundhub.presentation.pages.music.ui.pager.pages.main.sections.friend_music

import android.util.Log
import androidx.compose.foundation.interaction.collectIsDraggedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import com.soundhub.presentation.pages.music.ui.pager.pages.main.sections.friend_music.components.FriendMusicCard
import com.soundhub.presentation.pages.music.viewmodels.MusicViewModel
import com.soundhub.presentation.viewmodels.UiStateDispatcher

@Composable
fun FriendMusicSection(
	musicViewModel: MusicViewModel,
	uiStateDispatcher: UiStateDispatcher
) {
	val uiState by uiStateDispatcher.uiState.collectAsState(initial = UiState())
	val authorizedUser: User? = uiState.authorizedUser
	val friends = authorizedUser?.friends.orEmpty()

	val lazyListState = rememberLazyListState()
	val isRowDragged by lazyListState.interactionSource.collectIsDraggedAsState()

	val isSingle = friends.size == 1

	LaunchedEffect(key1 = isRowDragged) {
		Log.d("RecommendationSection", "isDragged $isRowDragged")

		musicViewModel.setPagerLock(isRowDragged)
	}

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
			modifier = Modifier
				.padding(top = 20.dp),
//				.pointerInput(isRowDragged) {
//					musicViewModel.horizontalDragHandler(this@pointerInput)
//				},
			horizontalArrangement = Arrangement.spacedBy(10.dp)
		) {
			items(items = friends, key = { it.id }) { friend ->
				FriendMusicCard(friend, isSingle)
			}
		}
	}
}