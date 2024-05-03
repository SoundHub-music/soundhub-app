package com.soundhub.ui.components.menu

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController
import com.soundhub.R
import com.soundhub.Route
import com.soundhub.data.model.Post
import com.soundhub.ui.viewmodels.PostViewModel
import java.util.UUID

@Composable
fun PostDropdownMenu(
    modifier: Modifier = Modifier,
    isMenuExpandedState: MutableState<Boolean>,
    postViewModel: PostViewModel,
    post: Post,
    navController: NavHostController,
    onDismissRequest: () -> Unit = {},
) {
    DropdownMenu(
        modifier = modifier,
        expanded = isMenuExpandedState.value,
        onDismissRequest = onDismissRequest
    ) {
        DropdownMenuItem(
            text = { Text(text = stringResource(id = R.string.delete_post_dropdown_menu)) },
            leadingIcon = { Icon(
                imageVector = Icons.Rounded.Delete, contentDescription = "delete post"
            ) },
            onClick = {
                onDeletePostMenuClick(postViewModel, post)
                isMenuExpandedState.value = false
            }
        )
        DropdownMenuItem(
            text = { Text(text = stringResource(id = R.string.edit_post_dropdown_menu)) },
            leadingIcon = { Icon(
                imageVector = Icons.Rounded.Edit,
                contentDescription = "edit post"
            )},
            onClick = { onEditPostMenuClick(navController = navController, postId = post.id) }
        )
    }
}

private fun onDeletePostMenuClick(
    postViewModel: PostViewModel,
    post: Post
) = postViewModel.deletePostById(post.id)

private fun onEditPostMenuClick(
    postId: UUID,
    navController: NavHostController
) = navController.navigate(Route.PostEditor.getStringRouteWithNavArg(postId.toString()))