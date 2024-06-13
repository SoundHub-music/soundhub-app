package com.soundhub.ui.friends.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CardColors
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
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
import com.soundhub.data.model.User
import com.soundhub.ui.components.avatar.CircularAvatar
import com.soundhub.ui.friends.FriendsUiState
import com.soundhub.ui.friends.FriendsViewModel
import com.soundhub.ui.friends.enums.FriendListPage
import com.soundhub.ui.profile.components.sections.user_main_data.getUserLocation
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch

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
        onClick = { navController
            .navigate(Route.Profile
                .getStringRouteWithNavArg(user.id.toString()))
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
                navController = navController,
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
    val usersCompatibility = friendsUiState.userCompatibilityPercentage
    val userCompatibility = remember(usersCompatibility) {
        usersCompatibility?.userCompatibilities.orEmpty()
            .filter { it.user.id == user.id }
            .firstNotNullOfOrNull { it }
    }

    Column(modifier = Modifier.fillMaxWidth(0.8f)) {
        Text(
            text = userFullName,
            fontWeight = FontWeight.Medium,
            fontSize = 20.sp,
            overflow = TextOverflow.Ellipsis
        )

        Text(
            text = when (chosenPage) {
                in listOf(FriendListPage.MAIN, FriendListPage.SEARCH) ->
                    getUserLocation(city = user.city, country = user.country)
                FriendListPage.RECOMMENDATIONS ->
                    // TODO: implement the logic of determining user similarity
                    stringResource(R.string.friends_recommendation_page_card_caption, userCompatibility?.compatibility ?: 0.0f)
                else -> ""
            },
            fontSize = 12.sp,
            fontWeight = FontWeight.ExtraLight,
            color = MaterialTheme.colorScheme.onBackground,
            textAlign = TextAlign.Left,

        )
    }
}

@Composable
private fun NavigateToChatButton(
    user: User,
    navController: NavHostController,
    friendsViewModel: FriendsViewModel
) {
    val coroutineScope = rememberCoroutineScope()

    FilledTonalIconButton(
        onClick = {
            coroutineScope.launch {
                friendsViewModel.getOrCreateChat(user)
                    .firstOrNull()
                    ?.let {
                        val route: String = Route.Messenger.Chat
                            .getStringRouteWithNavArg(it.id.toString())

                        navController.navigate(route)
                }
            }
        },
        shape = RoundedCornerShape(5.dp),
        modifier = Modifier,
    ) {
        Icon(
            painter = painterResource(id = R.drawable.baseline_forward_to_inbox_24),
            contentDescription = "send message"
        )
    }
}