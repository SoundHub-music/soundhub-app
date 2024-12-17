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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.net.toUri
import androidx.navigation.NavHostController
import com.soundhub.R
import com.soundhub.Route
import com.soundhub.domain.model.User
import com.soundhub.domain.states.FriendsUiState
import com.soundhub.presentation.pages.friends.FriendsViewModel
import com.soundhub.presentation.pages.friends.enums.FriendListPage
import com.soundhub.presentation.shared.avatar.CircularAvatar
import com.soundhub.utils.lib.UserUtils

@Composable
fun FriendCard(
	modifier: Modifier = Modifier,
	navController: NavHostController,
	friendsViewModel: FriendsViewModel,
	chosenPage: FriendListPage,
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
			navController
				.navigate(
					Route.Profile
						.getStringRouteWithNavArg(user.id.toString())
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
					chosenPage = chosenPage,
					friendsViewModel = friendsViewModel
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
	chosenPage: FriendListPage,
	friendsViewModel: FriendsViewModel
) {
	val userFullName = remember(user) { "${user.firstName} ${user.lastName}".trim() }
	val friendsUiState: FriendsUiState by friendsViewModel.friendsUiState.collectAsState()

	val userCompatibilities = friendsUiState.userCompatibilityPercentage
	val userCompatibility = remember(userCompatibilities) {
		userCompatibilities?.userCompatibilities.orEmpty()
			.filter { it.user.id == user.id }
			.firstNotNullOfOrNull { it }
	}

	val additionalText: String = when (chosenPage) {
		in listOf(FriendListPage.MAIN, FriendListPage.SEARCH) ->
			UserUtils.getUserLocation(city = user.city, country = user.country)

		FriendListPage.RECOMMENDATIONS ->
			stringResource(
				R.string.friends_recommendation_page_card_caption,
				userCompatibility?.compatibility ?: 0.0f
			)

		else -> ""
	}

	Column(modifier = Modifier.fillMaxWidth(0.8f)) {
		Text(
			text = userFullName,
			fontWeight = FontWeight.Medium,
			fontSize = 20.sp,
			overflow = TextOverflow.Ellipsis
		)

		if (additionalText.isNotEmpty())
			Text(
				text = additionalText,
				fontSize = 12.sp,
				fontWeight = FontWeight.ExtraLight,
				color = MaterialTheme.colorScheme.onBackground,
				textAlign = TextAlign.Left,
			)
	}
}