package com.soundhub.ui.friends

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.soundhub.UiStateDispatcher
import com.soundhub.data.model.User
import com.soundhub.ui.authentication.AuthenticationViewModel
import com.soundhub.ui.components.containers.ContentContainer
import com.soundhub.utils.SearchUtils

@Composable
fun FriendListScreen(
    uiStateDispatcher: UiStateDispatcher = hiltViewModel(),
    authViewModel: AuthenticationViewModel = hiltViewModel()
) {
    val friends = listOf(
        User(
            firstName = "Alexey",
            lastName = "Zaycev",
            avatarUrl = "https://img.freepik.com/free-psd/3d-illustration-human-avatar-profile_23-2150671122.jpg",
            country = "Russia",
            city = "Novosibirsk"
        ),
        User(
            firstName = "Nikolay",
            lastName = "Pupkin",
            avatarUrl = "https://img.freepik.com/free-psd/3d-illustration-human-avatar-profile_23-2150671122.jpg",
            country = "Russia",
            city = "Novosibirsk"
        ),
        User(
            firstName = "Alexey",
            lastName = "Zaycev",
            avatarUrl = "https://img.freepik.com/free-psd/3d-illustration-human-avatar-profile_23-2150671122.jpg",
            country = "Russia",
            city = "Novosibirsk"
        )
    )
    val uiState by uiStateDispatcher.uiState.collectAsState()
    val searchBarText: String = uiState.searchBarText
    var filteredFriendList by rememberSaveable { mutableStateOf(friends) }

    LaunchedEffect(key1 = searchBarText) {
        filteredFriendList = if (searchBarText.isNotEmpty()) {
            friends.filter{ SearchUtils.compareWithUsername(it, searchBarText) }
        } else friends
    }

    ContentContainer {
        LazyColumn(
            modifier = Modifier
        ) {
            items(items = filteredFriendList, key = { it.id }) {user ->
                FriendCard(user = user)
                HorizontalDivider(thickness = 1.dp)
            }
        }
    }
}


@Composable
@Preview(showBackground = true)
fun FriendListScreenPreview() {
    FriendListScreen()
}