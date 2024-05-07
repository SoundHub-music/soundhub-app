package com.soundhub.ui.friends.components.pages

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.soundhub.R
import com.soundhub.data.enums.ApiStatus
import com.soundhub.data.model.User
import com.soundhub.ui.friends.FriendsUiState
import com.soundhub.ui.friends.FriendsViewModel
import com.soundhub.ui.friends.components.FriendCard
import com.soundhub.ui.friends.enums.FriendListPage

@Composable
internal fun SearchUserPage(
    friendsViewModel: FriendsViewModel,
    navController: NavHostController
) {
    val friendsUiState: FriendsUiState by friendsViewModel.friendsUiState.collectAsState()
    val foundUsers: List<User> = friendsUiState.foundUsers
    val searchStatus: ApiStatus = friendsUiState.status

    if (searchStatus == ApiStatus.ERROR) {
        Text(
            text = stringResource(id = R.string.friends_search_users_not_found),
            textAlign = TextAlign.Center,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )
    }
    else if (foundUsers.isEmpty()) {
        Text(
            text = stringResource(id = R.string.friends_search_users_empty_screen),
            textAlign = TextAlign.Center,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )
    }
    else LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(5.dp)
    ) {
        items(items = foundUsers, key = { it.id }) { user ->
            FriendCard(
                navController = navController,
                friendsViewModel = friendsViewModel,
                chosenPage = FriendListPage.MAIN,
                user = user
            )
        }
    }
}