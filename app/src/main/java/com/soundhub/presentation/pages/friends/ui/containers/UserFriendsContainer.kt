package com.soundhub.presentation.pages.friends.ui.containers

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.soundhub.domain.model.User
import com.soundhub.presentation.pages.friends.FriendsViewModel
import com.soundhub.presentation.pages.friends.ui.cards.friend_card.FriendCard
import com.soundhub.utils.lib.UserUtils

@Composable
fun UserFriendsContainer(
	friendList: List<User>,
	navController: NavHostController,
	friendsViewModel: FriendsViewModel
) {
	LazyColumn(
		modifier = Modifier.fillMaxSize(),
		verticalArrangement = Arrangement.spacedBy(5.dp)
	) {
		items(items = friendList, key = { it.id }) { user ->
			FriendCard(
				user = user,
				navController = navController,
				friendsViewModel = friendsViewModel,
				additionalInfo = UserUtils.getUserLocation(user.city, user.country)
			)
		}
	}
}