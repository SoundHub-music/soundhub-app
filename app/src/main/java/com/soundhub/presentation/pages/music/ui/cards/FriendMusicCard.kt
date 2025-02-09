package com.soundhub.presentation.pages.music.ui.cards

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.net.toUri
import com.soundhub.domain.model.User
import com.soundhub.presentation.shared.avatar.CircularAvatar

@Composable
internal fun FriendMusicCard(
	user: User,
	isSingle: Boolean = false
) {
	val configuration = LocalConfiguration.current
	val cardWidth: Double = if (isSingle) configuration.smallestScreenWidthDp.toDouble() / 1.15
	else (configuration.screenWidthDp / 1.5)

	ElevatedCard(
		modifier = Modifier.fillMaxWidth()
	) {
		Column(modifier = Modifier.fillMaxWidth()) {
			Row(
				horizontalArrangement = Arrangement.spacedBy(10.dp),
				verticalAlignment = Alignment.CenterVertically,
				modifier = Modifier
					.background(MaterialTheme.colorScheme.secondaryContainer)
					.padding(10.dp)
					.defaultMinSize(minWidth = cardWidth.dp)
			) {
				CircularAvatar(
					imageUri = user.avatarUrl?.toUri(),
					modifier = Modifier.size(35.dp)
				)
				Text(
					text = user.getFullName(),
					fontWeight = FontWeight.Bold,
					fontSize = 18.sp
				)
			}

			LazyColumn(
				modifier = Modifier
					.fillMaxWidth()
					.padding(vertical = 10.dp, horizontal = 8.dp),
				verticalArrangement = Arrangement.spacedBy(10.dp)
			) {
				items(items = user.favoriteArtists, key = { it.id }) { artist ->
					OutlinedCard(
						onClick = {},
						modifier = Modifier.fillMaxWidth(),
						shape = RoundedCornerShape(5.dp)
					) {
						Row(
							modifier = Modifier
								.width(cardWidth.dp)
								.padding(8.dp),
							verticalAlignment = Alignment.CenterVertically,
							horizontalArrangement = Arrangement.spacedBy(10.dp)
						) {
							CircularAvatar(
								imageUri = artist.cover?.toUri(),
								shape = RoundedCornerShape(5.dp)
							)
							Text(
								text = artist.name ?: "",
								fontSize = 16.sp
							)
						}
					}
				}
			}
		}
	}
}