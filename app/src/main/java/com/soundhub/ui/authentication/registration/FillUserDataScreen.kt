package com.soundhub.ui.authentication.registration

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.soundhub.R
import com.soundhub.ui.components.buttons.FloatingNextButton
import com.soundhub.ui.components.forms.UserDataForm

@Composable
fun FillUserDataScreen(
    modifier: Modifier = Modifier,
    registrationViewModel: RegistrationViewModel
) {
    val registerState = registrationViewModel.registerState

    Box(
        modifier = modifier
            .background(MaterialTheme.colorScheme.background)
    ) {
       Column(
           modifier = Modifier
               .padding(vertical = 20.dp, horizontal = 16.dp)
       ) {
           Text(
               text = stringResource(id = R.string.screen_title_fill_user_data),
               fontWeight = FontWeight.Black,
               fontSize = 32.sp,
               lineHeight = 42.sp
           )

            UserDataForm(
                formStateFlow = registerState,
                onFirstNameChange = registrationViewModel::setFirstName,
                onLastNameChange = registrationViewModel::setLastName,
                onBirthdayChange = registrationViewModel::setBirthday,
                onGenderChange = registrationViewModel::setGender,
                onDescriptionChange = registrationViewModel::setDescription,
                onCountryChange = registrationViewModel::setCountry,
                onCityChange = registrationViewModel::setCity,
                onLanguagesChange = registrationViewModel::setLanguages,
                onAvatarChange = registrationViewModel::setAvatar
            )
        }

        FloatingNextButton(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp)
        ) {
            registrationViewModel
            .onPostRegisterNextBtnClick()
        }
    }
}