package com.soundhub.presentation.pages.music.widgets.sheet.service_details

import android.net.Uri
import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.net.toUri
import com.soundhub.R
import com.soundhub.data.api.responses.lastfm.LastFmFullUser
import com.soundhub.presentation.pages.music.viewmodels.LastFmLoginViewModel
import com.soundhub.presentation.pages.music.widgets.sheet.components.SummaryDetails
import com.soundhub.presentation.shared.avatar.CircularAvatar

@OptIn(ExperimentalLayoutApi::class)
@Composable
internal fun LastFmDetails(
	lastFmLoginViewModel: LastFmLoginViewModel
) {
	val lastFmUser: LastFmFullUser? by lastFmLoginViewModel.userInfo.collectAsState()
	val uriHandler = LocalUriHandler.current
	val lastFmUserAvatar: Uri? = lastFmUser?.images
		?.firstOrNull { it.size == "medium" }
		?.url?.toUri()

	LaunchedEffect(key1 = lastFmUser) {
		Log.d("details", lastFmUser.toString())
	}

	Column(modifier = Modifier.fillMaxWidth()) {
		Row(horizontalArrangement = Arrangement.spacedBy(20.dp)) {
			CircularAvatar(
				imageUri = lastFmUserAvatar,
				modifier = Modifier
					.size(84.dp)
					.offset(y = (-30).dp)
			)

			Column {
				Text(
					text = lastFmUser?.name ?: "",
					style = TextStyle(
						fontWeight = FontWeight.Bold,
						fontSize = 20.sp
					)
				)

				TextButton(
					onClick = { uriHandler.openUri(lastFmUser?.userLink ?: "") },
					contentPadding = PaddingValues(5.dp),
					shape = RoundedCornerShape(10.dp)
				) {
					Text(text = stringResource(id = R.string.last_fm_follow_profile))
				}
			}
		}

		lastFmUser?.let { user ->
			FlowRow {
				SummaryDetails(title = {
					Text(
						text = stringResource(
							id = R.string.last_fm_scrobble_track_count
						),
					)
				}) {
					Text(
						text = user.playCount,
						fontWeight = FontWeight.Black
					)
				}
				SummaryDetails(title = {
					Text(
						text = stringResource(
							id = R.string.last_fm_scrobble_artist_count
						)
					)
				}) {
					Text(
						text = user.artistCount,
						fontWeight = FontWeight.Black
					)
				}

			}

		}
	}
}