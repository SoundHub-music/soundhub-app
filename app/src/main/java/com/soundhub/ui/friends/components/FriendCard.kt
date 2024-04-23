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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.soundhub.R
import com.soundhub.Route
import com.soundhub.data.model.User
import com.soundhub.ui.components.avatar.CircularAvatar
import com.soundhub.ui.friends.enums.FriendListPage
import com.soundhub.ui.profile.components.getUserLocation

@Composable
fun FriendCard(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    user: User,
    chosenPage: FriendListPage
) {
    ElevatedCard(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardColors(
            containerColor = MaterialTheme.colorScheme.background,
            contentColor = MaterialTheme.colorScheme.onBackground,
            disabledContentColor = MaterialTheme.colorScheme.onSecondaryContainer,
            disabledContainerColor = MaterialTheme.colorScheme.secondaryContainer
        )
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
                    imageUrl = user.avatarUrl,
                    modifier = Modifier.size(64.dp)
                )
                Column(modifier = Modifier) {
                    Text(
                        text = "${user.firstName} ${user.lastName}".trim(),
                        fontWeight = FontWeight.Medium,
                        fontSize = 20.sp,
                    )

                    Text(
                        text = when (chosenPage) {
                            FriendListPage.MAIN ->
                                getUserLocation(city = user.city, country = user.country)
                            FriendListPage.RECOMMENDATIONS ->
                                // TODO: implement the logic of determining user similarity
                                stringResource(R.string.friends_recommendation_page_card_caption, 98)
                        },
                        fontSize = 12.sp,
                        fontWeight = FontWeight.ExtraLight,
                        color = MaterialTheme.colorScheme.onBackground,
                        textAlign = TextAlign.Left,
                    )
                }

            }
            FilledTonalIconButton(
                onClick = {
                    navController.navigate(Route.Messenger.Chat(user.id.toString()).route)
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
    }
}

@Composable
@Preview
fun FriendCardPreview() {
    val navController = rememberNavController()
    val user = User(
        firstName = "Alexey",
        lastName = "Zaycev",
        avatarUrl = "https://img.freepik.com/free-psd/3d-illustration-human-avatar-profile_23-2150671122.jpg",
        country = "Russia",
        city = "Novosibirsk"
    )
    FriendCard(
        user = user,
        navController = navController,
        chosenPage = FriendListPage.MAIN
    )
}