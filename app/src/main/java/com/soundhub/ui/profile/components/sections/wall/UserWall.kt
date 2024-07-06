package com.soundhub.ui.profile.components.sections.wall

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.soundhub.R
import com.soundhub.data.enums.ApiStatus
import com.soundhub.data.model.Post
import com.soundhub.data.model.User
import com.soundhub.ui.components.loaders.CircleLoader
import com.soundhub.ui.components.post_card.PostCard
import com.soundhub.ui.profile.ProfileUiState
import com.soundhub.ui.profile.ProfileViewModel
import com.soundhub.ui.profile.components.SectionLabel
import com.soundhub.ui.viewmodels.PostViewModel
import com.soundhub.ui.viewmodels.UiStateDispatcher

@Composable
internal fun UserWall(
    navController: NavHostController,
    uiStateDispatcher: UiStateDispatcher,
    profileViewModel: ProfileViewModel,
    postViewModel: PostViewModel = hiltViewModel(),
    lazyListScope: LazyListScope
) {
    val profileUiState: ProfileUiState by profileViewModel.profileUiState.collectAsState()
    val profileOwner: User? = profileUiState.profileOwner
    val posts: List<Post> = profileUiState.userPosts
    val isLoading = profileUiState.postStatus == ApiStatus.LOADING

    LaunchedEffect(profileOwner) {
        profileViewModel.loadPostsByUser()
        Log.d("UserWall", posts.toString())
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 10.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        SectionLabel(
            text = stringResource(id = R.string.profile_screen_user_posts),
            labelIcon = painterResource(id = R.drawable.round_sticky_note_2_24),
            iconTint = Color(0xFFFFC107),
        )

        if (isLoading)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) { CircleLoader(modifier = Modifier.padding(top = 10.dp)) }
        else if (posts.isEmpty())
            Text(
                text = stringResource(id = R.string.empty_postline_screen),
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth().padding(top = 20.dp)
            )
        else lazyListScope.items(items = posts, key = { it.id }) { post ->
            PostCard(
                post = post,
                navController = navController,
                uiStateDispatcher = uiStateDispatcher,
                postViewModel = postViewModel,
                onDeletePost = { id -> profileViewModel.deletePostById(id) },
                onLikePost = { id -> profileViewModel.togglePostLikeAndUpdatePostList(id) }
            )
        }
    }
}