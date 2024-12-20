package com.soundhub.presentation.pages.friends.ui.cards.friend_card

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CardColors
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.net.toUri
import androidx.navigation.NavHostController
import com.soundhub.Route
import com.soundhub.domain.model.User
import com.soundhub.presentation.pages.friends.FriendsViewModel
import com.soundhub.presentation.shared.avatar.CircularAvatar

@Composable
fun FriendCard(
	modifier: Modifier = Modifier,
	navController: NavHostController,
	friendsViewModel: FriendsViewModel,
	additionalInfo: String? = null,
	user: User,
) {
	ElevatedCard(
		modifier = modifier.fillMaxWidth(),
		shape = RoundedCornerShape(12.dp),
		colors = CardColors(
			containerColor = MaterialTheme.colorScheme.background,
			contentColor = MaterialTheme.colorScheme.onBackground,
			disabledContentColor = MaterialTheme.colorScheme.onSecondaryContainer,
			disabledContainerColor = MaterialTheme.colorScheme.secondaryContainer
		),
		onClick = {
			navController.navigate(
				Route.Profile.getStringRouteWithNavArg(user.id.toString())
			)
		}
	) {
		Row(
			modifier = Modifier
				.fillMaxWidth()
				.padding(16.dp),
			horizontalArrangement = Arrangement.SpaceBetween,
			verticalAlignment = Alignment.CenterVertically
		) {
			Row(
				modifier = Modifier,
				horizontalArrangement = Arrangement.spacedBy(16.dp),
				verticalAlignment = Alignment.CenterVertically
			) {
				CircularAvatar(
					imageUri = user.avatarUrl?.toUri(),
					modifier = Modifier.size(64.dp)
				)

				UserDescriptionColumn(
					user = user,
					additionalInfo = additionalInfo,
				)
			}

			NavigateToChatButton(
				user = user,
				friendsViewModel = friendsViewModel
			)
		}
	}
}

@Composable
private fun UserDescriptionColumn(
	user: User,
	additionalInfo: String? = null,
) {
	Column(modifier = Modifier.fillMaxWidth(0.8f)) {
		Text(
			text = user.getFullName(),
			fontWeight = FontWeight.Medium,
			fontSize = 20.sp,
			overflow = TextOverflow.Ellipsis
		)

		if (additionalInfo?.isNotEmpty() == true)
			Text(
				text = additionalInfo,
				fontSize = 12.sp,
				lineHeight = 16.sp,
				fontWeight = FontWeight.ExtraLight,
				color = MaterialTheme.colorScheme.onBackground,
				textAlign = TextAlign.Left,
			)
	}
}