package com.soundhub.ui.components.post_card

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.MoreVert
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.net.toUri
import androidx.navigation.NavHostController
import com.soundhub.Route
import com.soundhub.data.model.Post
import com.soundhub.data.model.User
import com.soundhub.ui.components.avatar.CircularAvatar
import com.soundhub.ui.components.menu.PostDropdownMenu
import com.soundhub.utils.DateFormatter
import java.util.UUID

@Composable
internal fun PostHeader(
    modifier: Modifier = Modifier,
    currentUser: User?,
    navController: NavHostController,
    post: Post,
    onDeletePost: (UUID) -> Unit
) {
    val isPostMenuExpandedState: MutableState<Boolean> = rememberSaveable { mutableStateOf(false) }
    val userFullName: String = "${post.author?.firstName} ${post.author?.lastName}".trim()
    val isAuthorizedUserAuthor: Boolean = currentUser?.id == post.author?.id

    Box(contentAlignment = Alignment.TopEnd) {
        Row(
            modifier = modifier
                .padding(horizontal = 16.dp, vertical = 12.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                CircularAvatar(
                    imageUri = post.author?.avatarUrl?.toUri(),
                    contentDescription = userFullName,
                    modifier = Modifier,
                    onClick = {
                        val stringAuthorId = post.author?.id.toString()
                        val route: String = Route.Profile.getStringRouteWithNavArg(stringAuthorId)
                        navController.navigate(route)
                    }
                )

                Column {
                    Text(
                        text = userFullName,
                        fontSize = 16.sp,
                        lineHeight = 24.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 0.15.sp
                    )

                    Text(
                        text = DateFormatter.getRelativeDate(post.publishDate),
                        fontSize = 16.sp,
                        lineHeight = 20.sp,
                        fontWeight = FontWeight.Medium,
                        letterSpacing = 0.25.sp
                    )
                }
            }
            if (isAuthorizedUserAuthor) {
                IconButton(
                    onClick = { isPostMenuExpandedState.value = !isPostMenuExpandedState.value }
                ) {
                    Icon(
                        imageVector = Icons.Rounded.MoreVert,
                        contentDescription = "post parameters"
                    )
                    PostDropdownMenu(
                        isMenuExpandedState = isPostMenuExpandedState,
                        post = post,
                        onDeletePost = onDeletePost,
                        navController = navController
                    ) { isPostMenuExpandedState.value = false }
                }
            }
        }
    }
}