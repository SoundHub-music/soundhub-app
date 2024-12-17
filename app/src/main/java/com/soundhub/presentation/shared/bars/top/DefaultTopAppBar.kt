package com.soundhub.presentation.shared.bars.top

import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.soundhub.R
import com.soundhub.domain.states.UiState
import com.soundhub.presentation.pages.notifications.NotificationViewModel
import com.soundhub.presentation.viewmodels.UiStateDispatcher

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DefaultTopAppBar(
	topBarTitle: String?,
	navController: NavHostController,
	uiStateDispatcher: UiStateDispatcher,
	notificationViewModel: NotificationViewModel
) {
	val uiState: UiState by uiStateDispatcher.uiState.collectAsState(initial = UiState())
	val isSearchBarActive = uiState.isSearchBarActive

	TopAppBar(
		title = {
			if (!isSearchBarActive)
				Text(text = topBarTitle ?: "")
		},
		navigationIcon = {
			if (!isSearchBarActive)
				IconButton(onClick = { navController.popBackStack() }) {
					Icon(
						imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
						contentDescription = stringResource(id = R.string.btn_description_back),
						modifier = Modifier.size(24.dp)
					)
				}
		},
		actions = {
			TopBarActions(
				navController = navController,
				uiStateDispatcher = uiStateDispatcher,
				notificationViewModel = notificationViewModel
			)
		}
	)
}