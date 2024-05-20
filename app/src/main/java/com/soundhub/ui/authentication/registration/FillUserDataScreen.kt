package com.soundhub.ui.authentication.registration

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.soundhub.R
import com.soundhub.ui.components.buttons.FloatingNextButton
import com.soundhub.ui.components.forms.UserDataForm
import com.soundhub.ui.authentication.registration.states.RegistrationState

@Composable
fun FillUserDataScreen(
    modifier: Modifier = Modifier,
    registrationViewModel: RegistrationViewModel
) {
    val registerState: State<RegistrationState> = registrationViewModel
        .registerState.collectAsState()

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
                formState = registerState,
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