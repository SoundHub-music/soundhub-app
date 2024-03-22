package com.soundhub.ui.edit_profile

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.soundhub.data.model.User
import com.soundhub.ui.authentication.AuthenticationViewModel
import com.soundhub.ui.components.forms.UserDataForm

@Composable
fun EditUserProfileScreen(
    authorizedUser: User?,
    authViewModel: AuthenticationViewModel = hiltViewModel(),
    editUserProfileViewModel: EditUserProfileViewModel = hiltViewModel(),
) {
    LaunchedEffect(true) {
        editUserProfileViewModel.setUser(authorizedUser)
    }

    val formState = editUserProfileViewModel.formState.collectAsState()

    UserDataForm(
        formState = formState,
        authViewModel = authViewModel,
        onFirstNameChange = editUserProfileViewModel::onFirstNameChange,
        onLastNameChange = editUserProfileViewModel::onLastNameChange,
        onBirthdayChange = editUserProfileViewModel::onBirthdateChange,
        onDescriptionChange = editUserProfileViewModel::onDescriptionChange,
        onGenderChange = editUserProfileViewModel::onGenderChange,
        onCountryChange = editUserProfileViewModel::onCountryChange,
        onCityChange = editUserProfileViewModel::onCityChange,
        onAvatarChange = editUserProfileViewModel::onAvatarChange,
        onLanguagesChange = editUserProfileViewModel::onLanguagesChange
    )
}

@Composable
@Preview
fun EditUserProfileScreenPreview() {
    EditUserProfileScreen(authorizedUser = User())
}