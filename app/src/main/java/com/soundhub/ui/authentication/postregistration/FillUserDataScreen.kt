package com.soundhub.ui.authentication.postregistration

import android.net.Uri
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.soundhub.R
import com.soundhub.UiEventDispatcher
import com.soundhub.ui.authentication.AuthenticationViewModel
import com.soundhub.ui.components.AvatarPicker
import com.soundhub.ui.components.DatePicker
import com.soundhub.ui.components.NextButton
import com.soundhub.utils.Constants
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Composable
fun FillUserDataScreen(
    modifier: Modifier = Modifier,
    authViewModel: AuthenticationViewModel = hiltViewModel()
) {
    val uiEventDispatcher: UiEventDispatcher = hiltViewModel()

    val registerState = authViewModel.registerState.collectAsState()
    var avatarUri by remember { mutableStateOf<Uri?>(null) }
    val authFormState = authViewModel.authFormState.collectAsState()

    LaunchedEffect(registerState.value) {
        Log.d("user", registerState.value.toString())
    }

    LaunchedEffect(authFormState.value) {
        Log.d("auth_validation_state", authFormState.value.toString())
    }

    Box(
        modifier = modifier
            .background(MaterialTheme.colorScheme.background)
    ) {
       Column(
           modifier = Modifier
               .padding(top = 20.dp, bottom = 20.dp, start = 16.dp, end = 16.dp)
       ) {
           Text(
               text = stringResource(id = R.string.fill_data_page_title),
               fontWeight = FontWeight.Black,
               fontFamily = FontFamily(Font(R.font.nunito_black)),
               fontSize = 32.sp
           )

           Column(
               modifier = Modifier
                   .padding(start = 20.dp, end = 20.dp),
               verticalArrangement = Arrangement.spacedBy(10.dp)
           ) {
               AvatarPicker { avatarUri = it }
               OutlinedTextField(
                   modifier = Modifier.fillMaxWidth(),
                   value = registerState.value.firstName,
                   singleLine = true,
                   label = { Text(text = stringResource(id = R.string.text_field_name_placeholder)) },
                   onValueChange = { name -> authViewModel.onFirstNameTextFieldChange(name) },
                   isError = !registerState.value.isFirstNameValid,
                   supportingText = {
                       if (!registerState.value.isFirstNameValid)
                           Text(text = stringResource(id = R.string.registration_firstname_error_message))
                   },
               )

               OutlinedTextField(
                   modifier = Modifier.fillMaxWidth(),
                   value = registerState.value.lastName,
                   singleLine = true,
                   label = { Text(stringResource(id = R.string.text_field_last_name_placeholder)) },
                   onValueChange = { lastName -> authViewModel.onLastNameTextFieldChange(lastName) },
                   isError = !registerState.value.isLastNameValid,
                   supportingText = {
                        if (!registerState.value.isLastNameValid)
                            Text(text = stringResource(id = R.string.registration_lastname_error_message))
                   }
               )

                DatePicker(
                    modifier = Modifier.fillMaxWidth(),
                    value = if (registerState.value.birthday != null) registerState.value.birthday.toString() else "",
                    label = Constants.DATE_FORMAT,
                    onValueChange = { value ->
                        authViewModel.onBirthdayTextFieldChange(LocalDate.parse(
                                value, DateTimeFormatter.ofPattern(Constants.DATE_FORMAT)
                            )
                        )
                    },
                    isError = registerState.value.isBirthdayValid,
                    supportingText = {
                        if (!registerState.value.isBirthdayValid)
                            Text(text = stringResource(id = R.string.registration_birthday_error_message))
                    }
                )

               OutlinedTextField(
                   modifier = Modifier
                       .fillMaxWidth()
                       .height(200.dp),
                   label = { Text(text = stringResource(id = R.string.text_field_description_placeholder)) },
                   onValueChange = { registerState.value.description = it },
                   value = registerState.value.description,
               )
           }
       }

        NextButton(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp)
        ) {
            authViewModel.onPostRegisterNextButtonClick(uiEventDispatcher.currentRoute.value)
        }
    }
}

@Composable
@Preview
fun FillUserDataScreenPreview() {
    FillUserDataScreen()
}
