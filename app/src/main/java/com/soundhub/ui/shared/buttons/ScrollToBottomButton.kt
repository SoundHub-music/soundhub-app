package com.soundhub.ui.shared.buttons

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.KeyboardArrowDown
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun BoxScope.ScrollToBottomButton(
	lazyListState: LazyListState,
	unreadMessageCount: Int,
	onClick: () -> Unit
) {
	AnimatedVisibility(
		visible = lazyListState.canScrollBackward,
		enter = scaleIn(),
		exit = scaleOut(),
		modifier = Modifier
			.align(Alignment.BottomStart)
			.padding(bottom = 10.dp),
	) {
		BadgedBox(
			badge = {
				if (unreadMessageCount > 0)
					Badge { Text(unreadMessageCount.toString()) }
			}
		) {
			FloatingActionButton(
				containerColor = MaterialTheme.colorScheme.tertiaryContainer,
				onClick = onClick
			) {
				Icon(
					imageVector = Icons.Rounded.KeyboardArrowDown,
					contentDescription = "scroll down"
				)
			}
		}
	}
}