package com.soundhub.ui.pages.post_editor

import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.Send
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.soundhub.R
import com.soundhub.data.model.User
import com.soundhub.data.states.PostEditorState
import com.soundhub.ui.pages.post_editor.components.ImagePreviewRow
import com.soundhub.utils.enums.ContentTypes
import java.util.UUID

@Composable
fun PostEditorScreen(
	postEditorViewModel: PostEditorViewModel = hiltViewModel(),
	profileOwner: User?,
	postId: UUID? = null
) {
	val postEditorState: PostEditorState by postEditorViewModel.postEditorState.collectAsState()

	val activityResultLauncher = rememberLauncherForActivityResult(
		contract = ActivityResultContracts.GetMultipleContents()
	) { postEditorViewModel.setImages(it.map { uri -> uri.toString() }) }

	LaunchedEffect(key1 = postId) {
		Log.d("PostEditorScreen", "post id: $postId")
		postId?.let { id -> postEditorViewModel.loadPost(id) }
	}

	LaunchedEffect(key1 = postEditorState) {
		Log.d("PostEditorScreen", "post editor state: $postEditorState")
	}

	Box(contentAlignment = Alignment.BottomEnd) {
		OutlinedTextField(
			modifier = Modifier.fillMaxSize(),
			value = postEditorState.content,
			onValueChange = { postEditorViewModel.setContent(it) },
			placeholder = {
				Text(
					text = stringResource(id = R.string.create_post_field_placeholder),
					fontSize = 20.sp
				)
			}
		)

		Column {
			ImagePreviewRow(postEditorViewModel)
			Row(
				modifier = Modifier
					.fillMaxWidth()
					.padding(horizontal = 10.dp),
				verticalAlignment = Alignment.CenterVertically,
				horizontalArrangement = Arrangement.SpaceBetween
			) {
				FloatingActionButton(
					onClick = { activityResultLauncher.launch(ContentTypes.IMAGE_ALL.type) },
					containerColor = MaterialTheme.colorScheme.secondaryContainer
				) {
					Icon(
						painter = painterResource(id = R.drawable.baseline_photo_camera_24),
						contentDescription = "add photo"
					)
				}

				FloatingActionButton(
					containerColor = MaterialTheme.colorScheme.secondaryContainer,
					modifier = Modifier.padding(10.dp),
					onClick = { postEditorViewModel.onSavePostClick(postId, profileOwner) }
				) {
					Icon(
						imageVector = Icons.AutoMirrored.Rounded.Send,
						contentDescription = "send post"
					)
				}
			}
		}
	}
}