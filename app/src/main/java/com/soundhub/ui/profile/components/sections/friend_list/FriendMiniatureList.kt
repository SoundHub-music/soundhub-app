package com.soundhub.ui.profile.components.sections.friend_list

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.soundhub.R

@Composable
fun FriendMiniatureList(friendList: List<FriendItem>) {
    Column(
        verticalArrangement = Arrangement.spacedBy(5.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            Text(
                text = "Друзья",
                fontFamily = FontFamily(Font(R.font.nunito_light)),
                fontWeight = FontWeight.Light,
                fontSize = 14.sp
            )
            Text(
                text = "${friendList.size}",
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily(Font(R.font.nunito_bold)),
                fontSize = 10.sp
            )
        }
        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            friendList.forEach {friend ->
                FriendMiniature(friend.avatarUrl)
            }
        }
    }
}