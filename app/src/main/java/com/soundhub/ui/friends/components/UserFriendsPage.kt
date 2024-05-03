package com.soundhub.ui.friends.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.soundhub.data.model.User
import com.soundhub.ui.friends.FriendsViewModel
import com.soundhub.ui.friends.enums.FriendListPage

@Composable
fun UserFriendsPage(
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