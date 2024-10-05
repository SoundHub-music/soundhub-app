package com.soundhub.ui.shared.bars.bottom

import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import com.soundhub.Route
import com.soundhub.ui.shared.bars.bottom.NavBarIconType.PainterIcon
import com.soundhub.ui.shared.bars.bottom.NavBarIconType.VectorIcon

data class NavBarMenuItem(
	val route: String = Route.PostLine.route,
	val icon: NavBarIconType,
)

sealed class NavBarIconType {
	data class VectorIcon(var icon: ImageVector): NavBarIconType()
	data class PainterIcon(var iconId: Int): NavBarIconType()
}

@Composable
fun NavBarIcon(navBarIcon: NavBarIconType, contentDescription: String?) {
	when (navBarIcon) {
		is VectorIcon -> Icon(
			imageVector = navBarIcon.icon,
			contentDescription = contentDescription
		)

		is PainterIcon -> Icon(
			painter = painterResource(navBarIcon.iconId),
			contentDescription = contentDescription
		)
	}
}