package com.soundhub.ui.authentication.postregistration

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.soundhub.R
import com.soundhub.ui.authentication.AuthenticationViewModel
import com.soundhub.ui.components.buttons.FloatingNextButton
import com.soundhub.ui.components.forms.UserDataForm
import com.soundhub.Route

@Composable
fun FillUserDataScreen(
    modifier: Modifier = Modifier,
    authViewModel: AuthenticationViewModel = hiltViewModel(),
    navController: NavHostController
) {
    val registerState = authViewModel.registerState.collectAsState()

    Box(
        modifier = modifier
            .background(MaterialTheme.colorScheme.background)
    ) {
       Column(
           modifier = Modifier
               .padding(top = 20.dp, bottom = 20.dp, start = 16.dp, end = 16.dp)
       ) {
           Text(
               text = stringResource(id = R.string.screen_title_fill_user_data),
               fontWeight = FontWeight.Black,
               fontFamily = FontFamily(Font(R.font.nunito_black)),
               fontSize = 32.sp,
               lineHeight = 42.sp
           )

            UserDataForm(
                formState = registerState,
                onFirstNameChange = authViewModel::setFirstName,
                onLastNameChange = authViewModel::setLastName,
                onBirthdayChange = authViewModel::setBirthday,
                onGenderChange = authViewModel::setGender,
                onDescriptionChange = authViewModel::setDescription,
                onCountryChange = authViewModel::setCountry,
                onCityChange = authViewModel::setCity
            )
        }

        FloatingNextButton(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp)
        ) { authViewModel.onPostRegisterNextButtonClick(Route.Authentication.FillUserData) }
    }
}