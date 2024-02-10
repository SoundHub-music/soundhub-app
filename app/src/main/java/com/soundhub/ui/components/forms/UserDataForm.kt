package com.soundhub.ui.components.forms

import android.net.Uri
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.soundhub.R
import com.soundhub.ui.components.AvatarPicker
import com.soundhub.ui.components.fields.DatePicker
import com.soundhub.ui.components.fields.CountryDropdownField
import com.soundhub.ui.components.fields.GenderDropdownField
import com.soundhub.utils.Constants
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Composable
fun UserDataForm(
    formState: State<IUserDataFormState>,
    onFirstNameChange: (String) -> Unit = {},
    onLastNameChange: (String) -> Unit = {},
    onBirthdayChange: (LocalDate?) -> Unit = {},
    onDescriptionChange: (String) -> Unit = {},
    onGenderChange: (String) -> Unit = {},
    onCountryChange: (String) -> Unit = {}
) {
    val userDataFormViewModel: UserDataFormViewModel = hiltViewModel()
    var avatarUri by rememberSaveable { mutableStateOf<Uri?>(null) }

    Column(
        modifier = Modifier
            .padding(start = 20.dp, end = 20.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        AvatarPicker { avatarUri = it }
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = formState.value.firstName ?: "",
            singleLine = true,
            label = { Text(text = stringResource(id = R.string.text_field_name_placeholder)) },
            onValueChange = onFirstNameChange,
            isError = !formState.value.isFirstNameValid,
            supportingText = {
                if (!formState.value.isFirstNameValid)
                    Text(text = stringResource(id = R.string.registration_firstname_error_message))
            }
        )

        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = formState.value.lastName ?: "",
            singleLine = true,
            label = { Text(stringResource(id = R.string.text_field_last_name_placeholder)) },
            onValueChange = onLastNameChange,
            isError = !formState.value.isLastNameValid,
            supportingText = {
                if (!formState.value.isLastNameValid)
                    Text(text = stringResource(id = R.string.registration_lastname_error_message))
            }
        )

        GenderDropdownField(
            formState = formState,
            onGenderChange = onGenderChange
        )

        CountryDropdownField(
            formState = formState,
            onCountryChange = onCountryChange,
            userDataFormViewModel = userDataFormViewModel
        )

        DatePicker(
            modifier = Modifier.fillMaxWidth(),
            value = if (formState.value.birthday != null) formState.value.birthday.toString() else "",
            label = Constants.DATE_FORMAT,
            onValueChange = { value ->
                val date = LocalDate.parse(value, DateTimeFormatter.ofPattern(Constants.DATE_FORMAT))
                onBirthdayChange(date)
            },
            isError = !formState.value.isBirthdayValid,
            supportingText = {
                if (!formState.value.isBirthdayValid)
                    Text(text = stringResource(id = R.string.registration_birthday_error_message))
            }
        )

        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp),
            label = { Text(text = stringResource(id = R.string.text_field_description_placeholder)) },
            onValueChange = onDescriptionChange,
            value = formState.value.description ?: "",
        )
    }
}