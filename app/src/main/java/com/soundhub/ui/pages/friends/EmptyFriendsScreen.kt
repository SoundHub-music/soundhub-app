package com.soundhub.ui.pages.friends

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.pager.PagerState
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.soundhub.R

@OptIn(ExperimentalFoundationApi::class)
@Composable
internal fun EmptyFriendsScreen(
	selectedTabState: PagerState,
	friendsViewModel: FriendsViewModel
) {
	Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
		Column(
			horizontalAlignment = Alignment.CenterHorizontally,
			verticalArrangement = Arrangement.spacedBy(10.dp),
			modifier = Modifier.fillMaxWidth()
		) {
			Text(
				text = stringResource(id = R.string.friends_empty_list_message),
				fontSize = 20.sp,
				textAlign = TextAlign.Center,
				fontWeight = FontWeight.Bold,
				modifier = Modifier
					.fillMaxWidth()
			)

			Button(onClick = { friendsViewModel.handleSearchFriendClick(selectedTabState) }) {
				Text(
					text = stringResource(id = R.string.friends_empty_list_button_text)
				)
			}
		}
	}
}