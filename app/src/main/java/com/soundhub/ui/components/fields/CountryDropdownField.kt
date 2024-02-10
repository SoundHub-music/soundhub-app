package com.soundhub.ui.components.fields

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.soundhub.R
import com.soundhub.ui.components.forms.IUserDataFormState
import com.soundhub.ui.components.forms.UserDataFormViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CountryDropdownField(
    formState: State<IUserDataFormState>,
    onCountryChange: (String) -> Unit = {},
    userDataFormViewModel: UserDataFormViewModel = hiltViewModel()
) {
    var isCountryDropdownExpanded by rememberSaveable {
        mutableStateOf(formState.value.country?.isNotEmpty() ?: false)
    }

    val countries = userDataFormViewModel.countryList.collectAsState().value

    ExposedDropdownMenuBox(
        expanded = isCountryDropdownExpanded,
        onExpandedChange = { isCountryDropdownExpanded = !isCountryDropdownExpanded },
        modifier = Modifier.fillMaxWidth()
    ) {
        OutlinedTextField(
            value = formState.value.country ?: "",
            onValueChange = onCountryChange,
            label = { Text(text = stringResource(id = R.string.text_field_country_placeholder)) },
            modifier = Modifier
                .menuAnchor()
                .fillMaxWidth()
        )

        ExposedDropdownMenu(
            expanded = isCountryDropdownExpanded,
            onDismissRequest = { isCountryDropdownExpanded = false }
        ) {
            countries.forEach {
                DropdownMenuItem(
                    text = { Text(text = it.name.common) },
                    onClick = {
                        onCountryChange(it.name.common)
                        isCountryDropdownExpanded = false
                    }
                )
            }
        }
    }
}