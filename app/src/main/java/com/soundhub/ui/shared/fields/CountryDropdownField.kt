package com.soundhub.ui.shared.fields

import android.util.Log
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.soundhub.R
import com.soundhub.data.model.Country
import com.soundhub.ui.shared.forms.IUserDataFormState
import com.soundhub.ui.shared.forms.UserDataFormViewModel
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CountryDropdownField(
    formState: State<IUserDataFormState>,
    onCountryChange: (String) -> Unit = {},
    userDataFormViewModel: UserDataFormViewModel 
) {
    var isCountryDropdownExpanded by rememberSaveable { mutableStateOf(false) }

    val countries: List<Country> by userDataFormViewModel.countryList.collectAsState()
    val isLoading by userDataFormViewModel.isLoading.collectAsState()
    var filteredCountries: List<Country> by rememberSaveable { mutableStateOf(countries) }

    LaunchedEffect(key1 = isLoading, key2 = countries) {
        filteredCountries = countries
        Log.d("CountryDropdownField", "countries: $countries")
    }

    ExposedDropdownMenuBox(
        expanded = isCountryDropdownExpanded,
        onExpandedChange = { isCountryDropdownExpanded = !isCountryDropdownExpanded },
        modifier = Modifier.fillMaxWidth()
    ) {
        OutlinedTextField(
            value = formState.value.country ?: "",
            onValueChange = { c ->
                onCountryChange(c)
                filteredCountries = countries.filter {
                    it.translations.rus.common.lowercase(Locale.ROOT)
                        .startsWith(c.lowercase(Locale.ROOT))
                }
            },
            label = { Text(text = stringResource(id = R.string.text_field_country_label)) },
            modifier = Modifier
                .menuAnchor()
                .fillMaxWidth()
        )

        ExposedDropdownMenu(
            expanded = isCountryDropdownExpanded,
            onDismissRequest = { isCountryDropdownExpanded = false }
        ) {
            filteredCountries.forEach {
                DropdownMenuItem(
                    text = { Text(text = it.translations.rus.common) },
                    onClick = {
                        onCountryChange(it.translations.rus.common)
                        isCountryDropdownExpanded = false
                    }
                )
            }
        }
    }
}