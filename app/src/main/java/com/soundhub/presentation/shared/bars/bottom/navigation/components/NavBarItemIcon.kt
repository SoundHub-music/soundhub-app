package com.soundhub.presentation.shared.bars.bottom.navigation.components

import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import com.soundhub.presentation.shared.bars.bottom.navigation.model.NavBarIconType


@Composable
internal fun NavBarItemIcon(navBarIcon: NavBarIconType, contentDescription: String?) {
	when (navBarIcon) {
		is NavBarIconType.VectorIcon -> Icon(
			imageVector = navBarIcon.icon,
			contentDescription = contentDescription
		)

		is NavBarIconType.PainterIcon -> Icon(
			painter = painterResource(navBarIcon.iconId),
			contentDescription = contentDescription
		)
	}
}