package com.soundhub.presentation.shared.bars.bottom.navigation.model

import androidx.compose.ui.graphics.vector.ImageVector

sealed class NavBarIconType {
	data class VectorIcon(var icon: ImageVector) : NavBarIconType()
	data class PainterIcon(var iconId: Int) : NavBarIconType()
}