package com.soundhub.ui.components.bars.bottom

import androidx.compose.runtime.Composable
import com.soundhub.Route

data class NavBarItem(
    val route: String = Route.PostLine.route,
    val icon: @Composable () -> Unit,
)