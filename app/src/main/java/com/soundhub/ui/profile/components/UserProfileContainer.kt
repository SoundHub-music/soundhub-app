package com.soundhub.ui.profile.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.soundhub.data.model.User
import com.soundhub.ui.authentication.AuthenticationViewModel
import com.soundhub.ui.authentication.states.UserState
import com.soundhub.ui.profile.components.sections.favorite_genres.FavoriteGenresSection
import com.soundhub.ui.profile.components.sections.friend_list.FriendMiniatureList
import com.soundhub.ui.profile.components.sections.user_actions.ProfileButtonRow
import com.soundhub.ui.profile.components.sections.user_actions.UserNameWithDescription
import com.soundhub.ui.profile.components.sections.wall.UserWall
import com.soundhub.ui.viewmodels.UiStateDispatcher

@Composable
fun UserProfileContainer(
    user: User?,
    authViewModel: AuthenticationViewModel,
    navController: NavHostController,
    uiStateDispatcher: UiStateDispatcher,
) {
    val authorizedUser: UserState by authViewModel
        .userInstance
        .collectAsState()

    val isOriginProfile: Boolean = authorizedUser
        .current?.id == user?.id

    val userLocation: String = getUserLocation(user?.city, user?.country)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                color = MaterialTheme.colorScheme.primaryContainer,
                shape = RoundedCornerShape(topStart = 30.dp, topEnd = 30.dp)
            )
    ) {
        LazyColumn(
            modifier = Modifier
                .padding(start = 16.dp, end = 16.dp, top = 30.dp),
            verticalArrangement = Arrangement.spacedBy(15.dp)
        ) {
            item {
                Column {
                    UserNameWithDescription(user)
                    if (userLocation.isNotEmpty())
                        Text(
                            text = userLocation,
                            fontWeight = FontWeight.Light,
                            fontSize = 14.sp
                        )
                }
            }

            item {
                ProfileButtonRow(
                    isOriginProfile = isOriginProfile,
                    navController = navController,
                )
            }

            item {
                FriendMiniatureList(
                    friendList = authorizedUser.current?.friends ?: emptyList(),
                    navController = navController
                )
            }

            item {
                FavoriteGenresSection(
                    genreList = user?.favoriteGenres ?: emptyList(),
                    isOriginProfile = isOriginProfile,
                    navController = navController
                )
            }

            // unconfirmed item
//            HorizontalDivider(thickness = 1.dp)
//            UserPhotoCarousel(
//                images = listOf(
//                    "https://play-lh.googleusercontent.com/y_-anVKl3ID25Je02J1dseqlAm41N8pwI-Gad7aDxPIPss3d7hUYF8c08SNCtwSPW5g",
//                    "https://play-lh.googleusercontent.com/y_-anVKl3ID25Je02J1dseqlAm41N8pwI-Gad7aDxPIPss3d7hUYF8c08SNCtwSPW5g",
//                    "https://play-lh.googleusercontent.com/y_-anVKl3ID25Je02J1dseqlAm41N8pwI-Gad7aDxPIPss3d7hUYF8c08SNCtwSPW5g"
//                ),
//                navController = navController,
//                uiStateDispatcher = uiStateDispatcher
//            )

            item {
                user?.let {
                    HorizontalDivider(thickness = 1.dp)
                    UserWall(
                        navController = navController,
                        uiStateDispatcher = uiStateDispatcher,
                        user = it
                    )
                }
            }
        }
    }
}

internal fun getUserLocation(city: String?, country: String?): String {
    return if ((city == null && country == null) || (city!!.isEmpty() && country!!.isEmpty())) ""
    else if (country!!.isNotEmpty() && city.isEmpty()) country
    else "$country, $city"
}


