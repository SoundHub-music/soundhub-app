package com.soundhub.ui.friends

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.soundhub.data.model.User
import com.soundhub.ui.components.containers.ContentContainer

@Composable
fun FriendListScreen() {
    val friends = listOf(
        User(
            firstName = "Alexey",
            lastName = "Zaycev",
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
        ),
        User(
            firstName = "Alexey",
            lastName = "Zaycev",
            avatarUrl = "https://img.freepik.com/free-psd/3d-illustration-human-avatar-profile_23-2150671122.jpg",
            country = "Russia",
            city = "Novosibirsk"
        )
    )
    ContentContainer {
        LazyColumn(
            modifier = Modifier
        ) {
            items(items = friends, key = { it.id }) {user ->
                FriendCard(
                    user = user,
                    modifier = Modifier.fillMaxWidth()
                )
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