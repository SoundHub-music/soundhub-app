package com.soundhub.ui.profile.components.sections.friend_list

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.soundhub.data.model.User
import com.soundhub.ui.components.avatar.CircularAvatar

@Composable
internal fun FriendsMiniaturesRow(friendList: List<User>) {
    val maxFriendsMiniatureCount = 10

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy((-15).dp)
    ) {
        val friendsMiniatureCount = if (friendList.size > maxFriendsMiniatureCount)
            (maxFriendsMiniatureCount - 1)
        else friendList.size

        friendList.subList(0, friendsMiniatureCount)
            .forEach { friend ->
                CircularAvatar(
                    imageUrl = friend.avatarUrl,
                    modifier = Modifier.size(24.dp)
                )
            }
    }
}