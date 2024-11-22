package com.soundhub.ui.pages.music.components.tab_pages.library.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.soundhub.R
import com.soundhub.ui.pages.music.viewmodels.LastFmLoginViewModel
import com.soundhub.ui.pages.music.viewmodels.SpotifyLoginViewModel
import com.soundhub.ui.pages.music.viewmodels.VKLoginViewModel

@Composable
fun MusicServiceList(
	modifier: Modifier = Modifier,
	lastFmLoginViewModel: LastFmLoginViewModel = hiltViewModel(),
	spotifyLoginViewModel: SpotifyLoginViewModel = hiltViewModel(),
	vkLoginViewModel: VKLoginViewModel = hiltViewModel()
) {
	ElevatedCard(
		modifier = modifier,
		colors = CardDefaults.cardColors(
			containerColor = MaterialTheme.colorScheme.tertiaryContainer
		)
	) {
		Column(
			verticalArrangement = Arrangement.spacedBy(10.dp),
			horizontalAlignment = Alignment.CenterHorizontally,
			modifier = Modifier.padding(15.dp)
		) {
			Text(
				text = stringResource(id = R.string.music_auth_service_text),
				fontSize = 24.sp,
				fontWeight = FontWeight.Black,
				lineHeight = 28.sp,
			)

			Row(
				horizontalArrangement = Arrangement.SpaceBetween,
				modifier = Modifier.fillMaxWidth()
			) {
				MusicServiceButton(
					painter = painterResource(id = R.drawable.last_fm),
					musicServiceDialogViewModel = lastFmLoginViewModel,
				)
				MusicServiceButton(
					painter = painterResource(id = R.drawable.spotify),
					musicServiceDialogViewModel = spotifyLoginViewModel,
				)
				MusicServiceButton(
					painter = painterResource(id = R.drawable.vk_logo),
					musicServiceDialogViewModel = vkLoginViewModel,
				)
			}
		}
	}
}

@Composable
@Preview
private fun UnauthorizedLibraryItemsPreview() {
	MusicServiceList()
}
