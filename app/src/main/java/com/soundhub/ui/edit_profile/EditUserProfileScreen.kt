package com.soundhub.ui.edit_profile

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import com.soundhub.data.model.User
import com.soundhub.ui.authentication.AuthenticationViewModel
import com.soundhub.ui.components.forms.UserDataForm

@Composable
fun EditUserProfileScreen(
    authorizedUser: User?,
    authViewModel: AuthenticationViewModel,
    editUserProfileViewModel: EditUserProfileViewModel,
) {
    val formState = editUserProfileViewModel.formState.collectAsState()
    LaunchedEffect(true) {
        editUserProfileViewModel.setUser(authorizedUser)
    }

    UserDataForm(
        formState = formState,
        authViewModel = authViewModel,
        onFirstNameChange = editUserProfileViewModel::setFirstName,
        onLastNameChange = editUserProfileViewModel::setLastName,
        onBirthdayChange = editUserProfileViewModel::setBirthday,
        onDescriptionChange = editUserProfileViewModel::setDescription,
        onGenderChange = editUserProfileViewModel::setGender,
        onCountryChange = editUserProfileViewModel::setCountry,
        onCityChange = editUserProfileViewModel::setCity,
        onAvatarChange = editUserProfileViewModel::setAvatar,
        onLanguagesChange = editUserProfileViewModel::setLanguages
    )
}