package com.soundhub.ui.edit_profile.profile

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.hilt.navigation.compose.hiltViewModel
import com.soundhub.data.model.User
import com.soundhub.ui.components.forms.UserDataForm

@Composable
fun EditUserProfileScreen(
    authorizedUser: User?,
    editUserProfileViewModel: EditUserProfileViewModel = hiltViewModel()
) {
    val formState = editUserProfileViewModel.formState.collectAsState()
    var isLoading by rememberSaveable {
        mutableStateOf(true)
    }

    /*
    At the initial moment of time the user state doesn't have time to load.
    We wait until authorizedUser is loaded
     */
    LaunchedEffect(authorizedUser) {
        isLoading = false
    }

    if (!isLoading)
        UserDataForm(
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
}