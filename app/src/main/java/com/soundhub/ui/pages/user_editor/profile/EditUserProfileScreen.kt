package com.soundhub.ui.pages.user_editor.profile

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.soundhub.R
import com.soundhub.domain.model.User
import com.soundhub.ui.pages.user_editor.components.EditProfileTopBarButton
import com.soundhub.ui.shared.dialogs.DismissChangesDialog
import com.soundhub.ui.shared.forms.UserDataForm

@Composable
fun EditUserProfileScreen(
	authorizedUser: User?,
	editUserProfileViewModel: EditUserProfileViewModel = hiltViewModel(),
	navController: NavHostController,
) {
	val formState = editUserProfileViewModel.formState
	val isDialogOpened by editUserProfileViewModel.isDialogOpened.collectAsState(initial = false)
	var isLoading by rememberSaveable { mutableStateOf(true) }

	/*
		At the initial moment of time the user state doesn't have time to load.
		We wait until authorizedUser is loaded
	 */
	LaunchedEffect(authorizedUser) {
		authorizedUser?.let { isLoading = false }
	}

	EditUserProfileScaffold(editUserProfileViewModel) {
		if (!isLoading)
			UserDataForm(
				modifier = Modifier.padding(it),
				formStateFlow = formState,
				formHandler = editUserProfileViewModel.formHandler
			)

		if (isDialogOpened)
			DismissChangesDialog(
				navController = navController,
				updateDialogStateCallback = { state ->
					editUserProfileViewModel.setDialogVisibility(state)
				}
			)
	}
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun EditUserProfileScaffold(
	editUserProfileViewModel: EditUserProfileViewModel,
	content: @Composable (PaddingValues) -> Unit
) {
	Scaffold(
		topBar = {
			TopAppBar(
				title = { Text(text = stringResource(id = R.string.screen_title_edit_profile)) },
				navigationIcon = {
					IconButton(onClick = { editUserProfileViewModel.onTopNavigationButtonClick() }) {
						Icon(
							imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
							contentDescription = stringResource(id = R.string.btn_description_back),
							modifier = Modifier.size(24.dp)
						)
					}
				},
				actions = { EditProfileTopBarButton(editUserProfileViewModel) }
			)
		}
	) { content(it) }
}