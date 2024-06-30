package com.soundhub.ui.components.forms

import android.net.Uri
import android.util.Log
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import androidx.hilt.navigation.compose.hiltViewModel
import com.soundhub.R
import com.soundhub.ui.components.avatar.AvatarPicker
import com.soundhub.ui.components.fields.CountryDropdownField
import com.soundhub.ui.components.fields.DatePicker
import com.soundhub.ui.components.fields.GenderDropdownField
import com.soundhub.ui.components.fields.UserLanguagesField
import com.soundhub.utils.constants.Constants.DATE_FORMAT
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Composable
fun UserDataForm(
    modifier: Modifier = Modifier,
    formStateFlow: Flow<IUserDataFormState>,
    onFirstNameChange: (String) -> Unit = {},
    onLastNameChange: (String) -> Unit = {},
    onBirthdayChange: (LocalDate?) -> Unit = {},
    onAvatarChange: (Uri) -> Unit = {},
    onDescriptionChange: (String) -> Unit = {},
    onGenderChange: (String) -> Unit = {},
    onCountryChange: (String) -> Unit = {},
    onCityChange: (String) -> Unit = {},
    onLanguagesChange: (List<String>) -> Unit = {}
) {
    val userDataFormViewModel: UserDataFormViewModel = hiltViewModel()
    val avatarUri = rememberSaveable { mutableStateOf<Uri?>(null) }
    val formState: State<IUserDataFormState> = formStateFlow.collectAsState(initial = EmptyFormState())

    LaunchedEffect(formState.value) {
        Log.d("UserDataForm", formState.value.toString())
        avatarUri.value = formState.value.avatarUrl?.toUri()
    }

    LaunchedEffect(key1 = avatarUri.value) {
        avatarUri.value?.let { onAvatarChange(it) }
    }

    Column(
        modifier = modifier
            .padding(horizontal = 20.dp, vertical = 20.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        AvatarPicker(
            imageUriState = avatarUri,
            onImagePick = { avatarUri.value = it }
        )

        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = formState.value.firstName,
            onValueChange = onFirstNameChange,
            singleLine = true,
            label = { Text(text = stringResource(id = R.string.text_field_name_label)) },
            isError = !formState.value.isFirstNameValid,
            supportingText = {
                if (!formState.value.isFirstNameValid)
                    Text(text = stringResource(id = R.string.user_form_firstname_error_message))
            }
        )

        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = formState.value.lastName,
            onValueChange = onLastNameChange,
            singleLine = true,
            label = { Text(stringResource(id = R.string.text_field_last_name_label)) },
            isError = !formState.value.isLastNameValid,
            supportingText = {
                if (!formState.value.isLastNameValid)
                    Text(text = stringResource(id = R.string.user_form_lastname_error_message))
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

        if (formState.value.country?.isNotEmpty() == true)
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                label = { Text(text = stringResource(id = R.string.text_field_city)) },
                value = formState.value.city ?: "",
                onValueChange = onCityChange
            )

        UserLanguagesField(
            formState = formState,
            onLanguagesChange = onLanguagesChange
        )

        DatePicker(
            modifier = Modifier.fillMaxWidth(),
            value = formState.value.birthday,
            label = stringResource(id = R.string.text_field_birthdate),
            onValueChange = { value ->
                val date = LocalDate.parse(value, DateTimeFormatter.ofPattern(DATE_FORMAT))
                onBirthdayChange(date)
            },
            isError = !formState.value.isBirthdayValid,
            supportingText = {
                if (!formState.value.isBirthdayValid)
                    Text(text = stringResource(id = R.string.user_form_birthday_error_message))
            }
        )

        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp),
            label = { Text(text = stringResource(id = R.string.text_field_description_label)) },
            onValueChange = onDescriptionChange,
            value = formState.value.description ?: "",
        )
    }
}
