package com.soundhub.ui.profile.components.sections.friend_list

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.soundhub.R
import com.soundhub.ui.profile.components.SectionLabel
import com.soundhub.Route
import com.soundhub.data.model.User

@Composable
fun FriendMiniatureSection(
    profileOwner: User?,
    navController: NavHostController
) {
    val friendList: List<User> = profileOwner?.friends ?: emptyList()

    ElevatedCard(
        onClick = { onSectionClick(profileOwner, navController) },
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(10.dp),
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
            verticalArrangement = Arrangement.spacedBy(5.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                SectionLabel(
                    text = stringResource(id = R.string.profile_screen_friend_section_caption),
                    labelIcon = painterResource(id = R.drawable.round_groups_24),
                    iconTint = MaterialTheme.colorScheme.tertiary,
                )
                Text(
                    text = "${friendList.size}",
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                )
            }
            FriendsMiniaturesRow(friendList = friendList)
        }
    }

}

private fun onSectionClick(profileOwner: User?, navController: NavHostController) {
    val userId: String = profileOwner?.id.toString()
    val friendPage: Route = Route.Profile.Friends.getRouteWithNavArg(userId)
    navController.navigate(friendPage.route)
}