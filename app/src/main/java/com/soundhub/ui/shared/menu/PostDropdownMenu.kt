package com.soundhub.ui.shared.menu

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
import com.soundhub.domain.model.Post
import java.util.UUID

@Composable
fun PostDropdownMenu(
	modifier: Modifier = Modifier,
	isMenuExpandedState: MutableState<Boolean>,
	post: Post,
	navController: NavHostController,
	onDeletePost: (UUID) -> Unit,
	onDismissRequest: () -> Unit = {}
) {
	DropdownMenu(
		modifier = modifier,
		expanded = isMenuExpandedState.value,
		onDismissRequest = onDismissRequest
	) {
		DeletePostMenuItem(
			isMenuExpandedState = isMenuExpandedState,
			onDeletePost = onDeletePost,
			post = post
		)
		EditPostMenuItem(
			navController = navController,
			isMenuExpandedState = isMenuExpandedState,
			post = post
		)
	}
}

@Composable
private fun DeletePostMenuItem(
	isMenuExpandedState: MutableState<Boolean>,
	onDeletePost: (UUID) -> Unit = {},
	post: Post
) {
	DropdownMenuItem(
		text = { Text(text = stringResource(id = R.string.delete_post_dropdown_menu)) },
		leadingIcon = {
			Icon(
				imageVector = Icons.Rounded.Delete, contentDescription = "delete post"
			)
		},
		onClick = {
			onDeletePost(post.id)
			isMenuExpandedState.value = false
		}
	)
}

@Composable
private fun EditPostMenuItem(
	navController: NavHostController,
	isMenuExpandedState: MutableState<Boolean>,
	post: Post
) {
	DropdownMenuItem(
		text = { Text(text = stringResource(id = R.string.edit_post_dropdown_menu)) },
		leadingIcon = {
			Icon(
				imageVector = Icons.Rounded.Edit,
				contentDescription = "edit post"
			)
		},
		onClick = {
			isMenuExpandedState.value = false
			onEditPostMenuClick(navController = navController, postId = post.id)
		}
	)
}

private fun onEditPostMenuClick(
	postId: UUID,
	navController: NavHostController
) = navController.navigate(Route.PostEditor.getStringRouteWithNavArg(postId.toString()))