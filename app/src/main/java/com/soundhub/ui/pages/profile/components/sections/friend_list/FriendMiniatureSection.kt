package com.soundhub.ui.pages.profile.components.sections.friend_list

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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.soundhub.R
import com.soundhub.ui.pages.profile.components.SectionLabel
import com.soundhub.data.model.User
import com.soundhub.data.states.ProfileUiState
import com.soundhub.ui.pages.profile.ProfileViewModel

@Composable
fun FriendMiniatureSection(profileViewModel: ProfileViewModel) {
    val profileUiState: ProfileUiState by profileViewModel
        .profileUiState
        .collectAsState()

    val profileOwner: User? = profileUiState.profileOwner
    val friendList: List<User> = profileOwner?.friends.orEmpty()

    ElevatedCard(
        onClick = { profileViewModel.onFriendSectionClick() },
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
            FriendsMiniaturesRow(profileViewModel)
        }
    }
}