package com.soundhub.ui.edit_profile.profile

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
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
import com.soundhub.data.model.User
import com.soundhub.ui.components.forms.UserDataForm
import com.soundhub.ui.edit_profile.components.EditProfileTopBarButton

@Composable
fun EditUserProfileScreen(
    authorizedUser: User?,
    editUserProfileViewModel: EditUserProfileViewModel = hiltViewModel(),
    navController: NavHostController,
) {
    val formState = editUserProfileViewModel.formState.collectAsState()
    var isLoading by rememberSaveable { mutableStateOf(true) }
    val isDialogOpened: MutableState<Boolean> = rememberSaveable { mutableStateOf(false) }

    /*
    At the initial moment of time the user state doesn't have time to load.
    We wait until authorizedUser is loaded
     */
    LaunchedEffect(authorizedUser) {
        authorizedUser?.let { isLoading = false }
    }

    EditUserProfileScaffold(
        editUserProfileViewModel = editUserProfileViewModel,
        navController = navController,
        isDialogOpened = isDialogOpened
    ) {
        if (!isLoading)
            UserDataForm(
                modifier = Modifier.padding(it),
                formState = formState,
                onFirstNameChange = editUserProfileViewModel::setFirstName,
                onLastNameChange = editUserProfileViewModel::setLastName,
                onBirthdayChange = editUserProfileViewModel::setBirthday,
                onDescriptionChange = editUserProfileViewModel::setDescription,
                onGenderChange = editUserProfileViewModel::setGender,
                onCountryChange = editUserProfileViewModel::setCountry,
                onCityChange = editUserProfileViewModel::setCity,
                onAvatarChange = editUserProfileViewModel::setAvatar,
                onLanguagesChange = editUserProfileViewModel::setLanguages,
            )

        if (isDialogOpened.value)
            DismissChangesDialog(
                isDialogOpened = isDialogOpened,
                navController = navController
            )
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun EditUserProfileScaffold(
    editUserProfileViewModel: EditUserProfileViewModel,
    navController: NavHostController,
    isDialogOpened: MutableState<Boolean>,
    content: @Composable (PaddingValues) -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(id = R.string.screen_title_edit_profile)) },
                navigationIcon = {
                    IconButton(onClick = {
                        if (editUserProfileViewModel.hasStateChanges())
                            isDialogOpened.value = true
                        else navController.popBackStack()
                    }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                            contentDescription = stringResource(id = R.string.btn_description_back),
                            modifier = Modifier.size(24.dp)
                        )
                    }
                },
                actions = {
                    EditProfileTopBarButton(
                        editUserProfileViewModel = editUserProfileViewModel,
                        navController = navController
                    )
                }
            )
        }
    ) { content(it) }
}

@Composable
private fun DismissChangesDialog(
    isDialogOpened: MutableState<Boolean>,
    navController: NavHostController
) {
    AlertDialog(
        onDismissRequest = { isDialogOpened.value = false },
        dismissButton = {
            TextButton(onClick = { isDialogOpened.value = false }) {
                Text(text = stringResource(id = R.string.edit_profile_alert_dialog_dismiss_btn))
            }
        },
        confirmButton = {
            TextButton(onClick = {
                isDialogOpened.value = false
                navController.popBackStack()
            }) { Text(text = stringResource(id = R.string.edit_profile_alert_dialog_confirm_btn)) }
        },
        title = { Text(text = stringResource(id = R.string.edit_profile_alert_dialog_title)) },
        text = { Text(text = stringResource(id = R.string.edit_profile_alert_dialog_content)) }
    )
}
