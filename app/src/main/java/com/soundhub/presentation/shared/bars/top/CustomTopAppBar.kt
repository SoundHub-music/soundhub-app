package com.soundhub.presentation.shared.bars.top

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.soundhub.R
import com.soundhub.domain.states.UiState
import com.soundhub.presentation.pages.notifications.NotificationViewModel
import com.soundhub.presentation.shared.fields.TransparentSearchTextField
import com.soundhub.presentation.viewmodels.UiStateDispatcher

@Composable
fun CustomTopAppBar(
	topBarTitle: String?,
	uiStateDispatcher: UiStateDispatcher,
	navController: NavHostController,
	notificationViewModel: NotificationViewModel
) {
	topBarTitle?.let {
		AppHeader(
			modifier = Modifier.padding(0.dp),
			pageName = topBarTitle,
			uiStateDispatcher = uiStateDispatcher,
			actionButton = {
				TopBarActions(
					navController = navController,
					uiStateDispatcher = uiStateDispatcher,
					notificationViewModel = notificationViewModel
				)
			}
		)
	}
}

@Composable
private fun AppHeader(
	pageName: String?,
	modifier: Modifier = Modifier,
	actionButton: @Composable () -> Unit,
	uiStateDispatcher: UiStateDispatcher
) {
	val uiState: UiState by uiStateDispatcher.uiState.collectAsState(initial = UiState())
	val isSearchBarActive: Boolean = uiState.isSearchBarActive
	val inputValue: String = uiState.searchBarText

	Row(
		modifier = modifier
			.height(60.dp)
			.fillMaxWidth()
			.background(
				color = MaterialTheme.colorScheme.secondaryContainer,
				shape = RoundedCornerShape(bottomStart = 20.dp, bottomEnd = 20.dp)
			)
			.shadow(
				elevation = 4.dp,
				spotColor = Color(0x40000000),
				ambientColor = Color(0x40000000)
			)
			.padding(5.dp),
		verticalAlignment = Alignment.CenterVertically,
		horizontalArrangement = Arrangement.SpaceBetween
	) {
		if (isSearchBarActive)
			TransparentSearchTextField(
				value = inputValue,
				onValueChange = uiStateDispatcher::updateSearchBarText,
				modifier = Modifier.padding(horizontal = 10.dp),
				uiStateDispatcher = uiStateDispatcher
			)
		else Row(
			modifier = Modifier
				.padding(10.dp)
				.fillMaxWidth(),
			verticalAlignment = Alignment.CenterVertically,
			horizontalArrangement = Arrangement.SpaceBetween
		) {
			AppLogoWithPageName(pageName)
			actionButton()
		}
	}
}

@Composable
private fun AppLogoWithPageName(pageName: String?) {
	Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
		Image(
			modifier = Modifier
				.width(40.dp)
				.height(40.dp),
			painter = painterResource(id = R.drawable.soundhub_logo),
			contentDescription = "app logo"
		)
		if (pageName != null)
			Text(
				text = pageName,
				fontWeight = FontWeight.Bold,
				fontSize = 24.sp,
			)
	}
}