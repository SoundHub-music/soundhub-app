package com.soundhub.ui.components.fields

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.SuggestionChipDefaults
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
import com.soundhub.R
import com.soundhub.ui.components.forms.IUserDataFormState

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun UserLanguagesField(
    onLanguagesChange: (List<String>) -> Unit = {},
    formState: State<IUserDataFormState>
) {
    var languageText by rememberSaveable { mutableStateOf("") }

    Column(
        verticalArrangement = Arrangement.spacedBy(5.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            label = { Text(text = stringResource(id = R.string.text_field_languages)) },
            singleLine = true,
            keyboardActions = KeyboardActions(
                onDone = {
                    onLanguagesChange(formState.value.languages + languageText)
                    languageText = ""
                }
            ),
            value = languageText,
            onValueChange = { languageText = it },
        )
        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            formState.value.languages.forEach { lang ->
                SuggestionChip(
                    colors = SuggestionChipDefaults.suggestionChipColors(
                        containerColor = MaterialTheme.colorScheme.tertiaryContainer
                    ),
                    onClick = {
                        onLanguagesChange(formState.value.languages.filter { it != lang })
                    },
                    label = { Text(text = lang) }
                )
            }
        }
    }
}