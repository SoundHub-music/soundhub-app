package com.soundhub.ui.profile.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.material.icons.rounded.KeyboardArrowDown
import androidx.compose.material.icons.rounded.KeyboardArrowUp
import androidx.compose.material.icons.rounded.MailOutline
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconToggleButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.soundhub.R
import com.soundhub.data.model.Genre
import com.soundhub.data.model.User
import com.soundhub.ui.authentication.AuthenticationViewModel
import com.soundhub.ui.profile.components.sections.favorite_genres.FavoriteGenresSection
import com.soundhub.ui.profile.components.sections.friend_list.FriendItem
import com.soundhub.ui.profile.components.sections.friend_list.FriendMiniatureList
import com.soundhub.ui.profile.components.sections.photos.UserPhotoCarousel


@Composable
fun UserProfileContainer(user: User?, authViewModel: AuthenticationViewModel = hiltViewModel()) {
    val userCreds = authViewModel.userCreds.collectAsState(initial = null)
    val isOriginProfile: Boolean = user?.id?.equals(userCreds.value?.id) ?: false
    val actionProfileButtonContent = if (isOriginProfile)
        stringResource(id = R.string.edit_profile_btn_content)
    else stringResource(id = R.string.write_message_btn_content)

    val profileActionButtonIcon: ImageVector = if (isOriginProfile)
        Icons.Rounded.Edit
    else Icons.Rounded.MailOutline

    Column(
        verticalArrangement = Arrangement.spacedBy(15.dp)
    ) {
        Column {
            UserNameWithDescription(user)
            Text(
                text = "${user?.country}, ${user?.city}",
                fontWeight = FontWeight.Light,
                fontSize = 14.sp
            )
        }

        FilledTonalButton(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(10.dp),
            colors = ButtonDefaults.buttonColors(),
            onClick = { /* TODO: make update profile or write a message logic click */ }
        ) {
            Row(
                modifier = Modifier.height(30.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(imageVector = profileActionButtonIcon, contentDescription = "profile_action_button_icon")
                Text(
                    text = actionProfileButtonContent,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )
            }
        }

        // friend list with fake data
        FriendMiniatureList(friendList = listOf(FriendItem(null)))

        // genres list with fake data
        FavoriteGenresSection(
            genreList = listOf(
                Genre(name = "pop rock"),
                Genre(name = "alternative rock"),
                Genre(name = "jazz"),
                Genre(name = "pop"),
                Genre(name = "hip-hop"),
                Genre(name = "punk-rock"),
                Genre(name = "post-hardcore")
            )
        )

        // temporary user photos
        UserPhotoCarousel(photos = listOf(
            "https://play-lh.googleusercontent.com/y_-anVKl3ID25Je02J1dseqlAm41N8pwI-Gad7aDxPIPss3d7hUYF8c08SNCtwSPW5g",
            "https://play-lh.googleusercontent.com/y_-anVKl3ID25Je02J1dseqlAm41N8pwI-Gad7aDxPIPss3d7hUYF8c08SNCtwSPW5g",
            "https://play-lh.googleusercontent.com/y_-anVKl3ID25Je02J1dseqlAm41N8pwI-Gad7aDxPIPss3d7hUYF8c08SNCtwSPW5g"
        ))
    }
}


@Composable
private fun UserNameWithDescription(user: User? = null) {
    var isDescriptionButtonChecked: Boolean by rememberSaveable {
        mutableStateOf(false)
    }

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "${user?.firstName} ${user?.lastName}".trimIndent(),
            fontWeight = FontWeight.Bold,
            lineHeight = 16.sp,
            fontSize = 28.sp,
            modifier = Modifier
        )

        IconToggleButton(
            checked = isDescriptionButtonChecked,
            onCheckedChange = { isDescriptionButtonChecked = it },
        ) {
            Icon(
                imageVector =
                if (isDescriptionButtonChecked) Icons.Rounded.KeyboardArrowUp
                else Icons.Rounded.KeyboardArrowDown,
                contentDescription = "expand_description",
                modifier = Modifier.size(35.dp)
            )
        }
    }
    if (isDescriptionButtonChecked)
        UserDescriptionBlock(user)
}

@Composable
private fun UserDescriptionBlock(user: User? = null) {
    // TODO: expand the list of user data
    Column() {
        Text("О себе: ${user?.description}")
    }
}