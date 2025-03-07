package com.soundhub.presentation.pages.music.widgets.sheet

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.unit.dp
import com.soundhub.presentation.pages.music.viewmodels.MusicServiceViewModel

@Composable
fun MusicServiceSheetContainer(
	modifier: Modifier = Modifier,
	musicServiceViewModel: MusicServiceViewModel<*>,
	formIcon: Painter?,
	formIconDescription: String? = null,
) {
	Box(
		contentAlignment = Alignment.Center,
		modifier = modifier.padding(horizontal = 5.dp)
	) {
		LoginMusicServiceSheet(
			musicServiceViewModel = musicServiceViewModel,
			formIcon = formIcon,
			formIconDescription = formIconDescription,
		)
	}
}
