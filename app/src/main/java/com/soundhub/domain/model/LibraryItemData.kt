package com.soundhub.domain.model

import androidx.compose.ui.graphics.painter.Painter

data class LibraryItemData(
	val title: String,
	val route: String,
	val icon: Painter,
	val contentDescription: String? = null
)