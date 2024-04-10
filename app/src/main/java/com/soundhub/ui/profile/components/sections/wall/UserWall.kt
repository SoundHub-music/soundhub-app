package com.soundhub.ui.profile.components.sections.wall

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.soundhub.R
import com.soundhub.data.model.User
import com.soundhub.ui.authentication.AuthenticationViewModel
import com.soundhub.ui.components.CircleLoader
import com.soundhub.ui.postline.components.post_card.PostCard
import com.soundhub.ui.profile.components.SectionLabel
import com.soundhub.ui.states.PostUiState
import com.soundhub.ui.viewmodels.PostViewModel
import com.soundhub.ui.viewmodels.UiStateDispatcher

@Composable
internal fun UserWall(
    navController: NavHostController,
    uiStateDispatcher: UiStateDispatcher,
    postViewModel: PostViewModel = hiltViewModel(),
    user: User,
) {
    val postUiState: PostUiState by postViewModel.postUiState.collectAsState()

    LaunchedEffect(true) {
        postViewModel.getPostsByUser(user)
        Log.d("UserWall", postUiState.toString())
    }

    Column(
        modifier = Modifier
            .fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(10.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            SectionLabel(
                text = stringResource(id = R.string.profile_screen_user_posts),
                modifier = Modifier
            )
        }

        if (postUiState.isLoading)
            CircleLoader(modifier = Modifier.padding(top = 10.dp))
        else postUiState
            .posts.forEach {
            PostCard(
                post = it,
                navController = navController,
                uiStateDispatcher = uiStateDispatcher,
                currentUser = user,
                postViewModel = postViewModel
            )
        }
    }
}