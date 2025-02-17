package com.soundhub.presentation.pages.music.ui.buttons

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.soundhub.R
import com.soundhub.presentation.pages.music.viewmodels.MusicServiceBottomSheetViewModel
import com.soundhub.presentation.theme.acceptColor
import com.soundhub.presentation.theme.onAcceptColor

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MusicServiceButton(
	modifier: Modifier = Modifier,
	onClick: () -> Unit = {},
	painter: Painter,
	contentDescription: String? = null,
	bottomSheetViewModel: MusicServiceBottomSheetViewModel
) {
	val isAuthorised by bottomSheetViewModel.isAuthorizedState.collectAsState()

	LaunchedEffect(isAuthorised) {
		Log.d("MusicServiceButton", "isAuthorised: $isAuthorised")
	}

	BadgedBox(
		badge = {
			if (isAuthorised)
				Badge(
					modifier = Modifier
						.offset(x = (-15).dp)
						.size(28.dp),
					containerColor = MaterialTheme.colorScheme.acceptColor,
				) {
					Icon(
						painter = painterResource(id = R.drawable.round_how_to_reg_24),
						contentDescription = "authorized",
						tint = MaterialTheme.colorScheme.onAcceptColor
					)
				}
		}
	) {
		IconButton(
			onClick = onClick,
			modifier = modifier
				.background(
					color = MaterialTheme.colorScheme.primaryContainer,
					shape = RoundedCornerShape(10.dp)
				)
				.padding(10.dp)
				.size(70.dp)
		) {
			Image(
				painter = painter,
				contentDescription = contentDescription,
				modifier = Modifier.fillMaxSize(0.8f),
			)
		}
	}
}