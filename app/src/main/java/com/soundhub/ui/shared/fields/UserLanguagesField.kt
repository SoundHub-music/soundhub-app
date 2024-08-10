package com.soundhub.ui.shared.fields

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
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.soundhub.R
import com.soundhub.ui.shared.forms.IUserDataFormState

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun UserLanguagesField(
	onLanguagesChange: (List<String>) -> Unit = {},
	formState: State<IUserDataFormState>
) {
	// TODO: make language field more intuitive
	var languageText by rememberSaveable { mutableStateOf("") }
	val languages: List<String> = remember(formState.value) { formState.value.languages }

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
					if (languageText.isNotBlank()) {
						onLanguagesChange(languages + languageText)
						languageText = ""
					}
				}
			),
			value = languageText,
			onValueChange = { languageText = it },
		)
		FlowRow(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
			languages.forEach { lang ->
				SuggestionChip(
					colors = SuggestionChipDefaults.suggestionChipColors(
						containerColor = MaterialTheme.colorScheme.tertiaryContainer
					),
					onClick = { onLanguagesChange(formState.value.languages.filter { it != lang }) },
					label = { Text(text = lang) }
				)
			}
		}
	}
}