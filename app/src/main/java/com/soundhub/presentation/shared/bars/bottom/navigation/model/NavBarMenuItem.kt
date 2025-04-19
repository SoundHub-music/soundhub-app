package com.soundhub.presentation.shared.bars.bottom.navigation.model

import com.soundhub.Route

data class NavBarMenuItem(
	var route: String = Route.PostLine.route,
	val icon: NavBarIconType,
)

