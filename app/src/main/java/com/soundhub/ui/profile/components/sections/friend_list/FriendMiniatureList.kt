package com.soundhub.ui.profile.components.sections.friend_list

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.soundhub.R
import com.soundhub.ui.components.CircularAvatar
import com.soundhub.ui.profile.components.FriendMiniatureItem
import com.soundhub.ui.profile.components.SectionLabel
import com.soundhub.Route

@Composable
fun FriendMiniatureList(friendList: List<FriendMiniatureItem>, navController: NavHostController) {
    Column(
        verticalArrangement = Arrangement.spacedBy(5.dp),
        modifier = Modifier.clickable { navController.navigate(Route.FriendList.route) }
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {

            SectionLabel(text = stringResource(id = R.string.profile_screen_friend_section_caption))
            Text(
                text = "${friendList.size}",
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily(Font(R.font.nunito_bold)),
                fontSize = 14.sp
            )
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy((-15).dp)
        ) {
            friendList.subList(0, if (friendList.size > 10) 9 else friendList.size)
                .forEach { friend ->
                    CircularAvatar(
                        imageUrl = friend.avatarUrl,
                        modifier = Modifier.size(24.dp)
                    )
            }
        }
    }
}