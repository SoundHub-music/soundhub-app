package com.soundhub.ui.components.bars.bottom

import androidx.compose.ui.graphics.vector.ImageVector
import com.soundhub.Route

data class NavBarItem(
    val route: String = Route.PostLine.route,
    val icon: ImageVector,
)