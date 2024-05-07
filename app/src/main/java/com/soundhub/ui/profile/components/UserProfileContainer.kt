package com.soundhub.ui.profile.components

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.soundhub.data.model.User
import com.soundhub.ui.profile.ProfileUiState
import com.soundhub.ui.profile.ProfileViewModel
import com.soundhub.ui.profile.components.sections.favorite_genres.FavoriteGenresSection
import com.soundhub.ui.profile.components.sections.friend_list.FriendMiniatureSection
import com.soundhub.ui.profile.components.sections.user_actions.ProfileButtonsSection
import com.soundhub.ui.profile.components.sections.user_main_data.UserMainDataSection
import com.soundhub.ui.profile.components.sections.wall.UserWall
import com.soundhub.ui.viewmodels.UiStateDispatcher

@Composable
fun UserProfileContainer(
    profileOwner: User?,
    navController: NavHostController,
    uiStateDispatcher: UiStateDispatcher,
    profileViewModel: ProfileViewModel
) {
    val profileUiState: ProfileUiState by profileViewModel.profileUiState.collectAsState()
    val authorizedUser: User? = profileUiState.authorizedUser
    var isOriginProfile: Boolean by rememberSaveable {
        mutableStateOf(authorizedUser?.id == profileOwner?.id)
    }

    LaunchedEffect(key1 = authorizedUser, key2 = profileOwner) {
        isOriginProfile = authorizedUser?.id == profileOwner?.id
        Log.d("UserProfileContainer", "authorized user: $authorizedUser")
    }

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
            item { UserMainDataSection(profileOwner) }
            item {
                ProfileButtonsSection(
                    profileViewModel = profileViewModel,
                    isOriginProfile = isOriginProfile,
                    navController = navController,
                    profileOwner = profileOwner
                )
            }
            item {
                FriendMiniatureSection(
                    profileOwner = profileOwner,
                    navController = navController
                )
            }
            item {
                FavoriteGenresSection(
                    genreList = profileOwner?.favoriteGenres ?: emptyList(),
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
                profileOwner?.let {
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