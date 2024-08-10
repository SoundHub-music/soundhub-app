package com.soundhub.ui.pages.friends.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.soundhub.data.model.User
import com.soundhub.ui.pages.friends.FriendsViewModel
import com.soundhub.ui.pages.friends.components.friend_card.FriendCard
import com.soundhub.ui.pages.friends.enums.FriendListPage

@Composable
fun UserFriendsContainer(
	friendList: List<User>,
	navController: NavHostController,
	chosenPage: FriendListPage,
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
				chosenPage = chosenPage,
				friendsViewModel = friendsViewModel
			)
		}
	}
}