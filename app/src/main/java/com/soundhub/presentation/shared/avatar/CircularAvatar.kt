package com.soundhub.presentation.shared.avatar

import android.net.Uri
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.soundhub.R
import com.soundhub.utils.enums.MediaFolder
import com.soundhub.utils.lib.ImageUtils

@Composable
fun CircularAvatar(
	modifier: Modifier = Modifier,
	imageUri: Uri?,
	shape: Shape = CircleShape,
	contentDescription: String? = null,
	onClick: () -> Unit = {}
) {
	val modelUrl: String? = remember(imageUri) {
		ImageUtils.getFileUrlOrLocalPath(imageUri, MediaFolder.AVATAR)
	}

	Box(
		modifier = modifier
			.clip(shape)
			.size(40.dp),
		contentAlignment = Alignment.Center
	) {
		AsyncImage(
			model = modelUrl,
			contentDescription = contentDescription,
			modifier = Modifier
				.clip(shape)
				.fillMaxSize()
				.clickable { onClick() },
			contentScale = ContentScale.Crop,
			error = painterResource(R.drawable.user_placeholder)
		)
	}
}

@Composable
@Preview
private fun CircularAvatarPreview() {
	val imageState: MutableState<Uri?> = remember { mutableStateOf(null) }
	CircularAvatar(imageUri = imageState.value)
}