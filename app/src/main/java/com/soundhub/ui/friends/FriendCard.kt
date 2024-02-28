package com.soundhub.ui.friends

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CardColors
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.soundhub.data.model.User
import com.soundhub.ui.components.CircularAvatar
import com.soundhub.ui.profile.components.getUserLocation

@Composable
fun FriendCard(
    modifier: Modifier = Modifier,
    user: User? = null
) {
    ElevatedCard(
        modifier = modifier,
        shape = RoundedCornerShape(5.dp),
        colors = CardColors(
            containerColor = MaterialTheme.colorScheme.background,
            contentColor = MaterialTheme.colorScheme.onBackground,
            disabledContentColor = MaterialTheme.colorScheme.onSecondaryContainer,
            disabledContainerColor = MaterialTheme.colorScheme.secondaryContainer
        )
    ) {
        Row(
            modifier = Modifier.padding(10.dp),
            horizontalArrangement = Arrangement.spacedBy(15.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            CircularAvatar(
                imageUrl = user?.avatarUrl,
                modifier = Modifier.size(72.dp)
            )
            Column(modifier = Modifier) {
                Text(
                    text = "${user?.firstName} ${user?.lastName}".trim(),
                    fontWeight = FontWeight.Medium,
                    fontSize = 20.sp,
                )

                Text(
                    text = getUserLocation(city = user?.city, country = user?.country),
                    fontSize = 10.sp,
                    fontWeight = FontWeight.ExtraLight,
                    color = MaterialTheme.colorScheme.onBackground,
                    textAlign = TextAlign.Left,
                )

                TextButton(
                    onClick = { /*TODO*/ },
                    shape = RoundedCornerShape(5.dp),
                    modifier = Modifier
                ) {
                    Text(
                        text = "Написать сообщение",
                        fontSize = 10.sp,
                    )
                }
            }
        }
    }
}

@Composable
@Preview
fun FriendCardPreview() {
    val user = User(
        firstName = "Alexey",
        lastName = "Zaycev",
        avatarUrl = "https://img.freepik.com/free-psd/3d-illustration-human-avatar-profile_23-2150671122.jpg",
        country = "Russia",
        city = "Novosibirsk"
    )
    FriendCard(user = user)
}