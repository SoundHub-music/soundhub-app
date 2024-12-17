package com.soundhub.presentation.shared.bars.bottom.navigation.model

import com.soundhub.Route

data class NavBarMenuItem(
	val route: String = Route.PostLine.route,
	val icon: NavBarIconType,
)

