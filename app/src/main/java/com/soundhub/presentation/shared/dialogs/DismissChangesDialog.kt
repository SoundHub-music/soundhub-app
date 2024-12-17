package com.soundhub.presentation.shared.dialogs

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController
import com.soundhub.R

@Composable
fun DismissChangesDialog(
	navController: NavHostController,
	updateDialogStateCallback: (Boolean) -> Unit
) {
	AlertDialog(
		onDismissRequest = { updateDialogStateCallback(false) },
		dismissButton = {
			TextButton(onClick = { updateDialogStateCallback(false) }) {
				Text(text = stringResource(id = R.string.edit_profile_alert_dialog_dismiss_btn))
			}
		},
		confirmButton = {
			TextButton(onClick = {
				updateDialogStateCallback(false)
				navController.popBackStack()
			}) { Text(text = stringResource(id = R.string.edit_profile_alert_dialog_confirm_btn)) }
		},
		title = { Text(text = stringResource(id = R.string.edit_profile_alert_dialog_title)) },
		text = { Text(text = stringResource(id = R.string.edit_profile_alert_dialog_content)) }
	)
}
