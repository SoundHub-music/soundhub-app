package com.soundhub.ui.shared.fields

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MenuAnchorType
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
import com.soundhub.R
import com.soundhub.data.enums.Gender
import com.soundhub.ui.shared.forms.IUserDataFormState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GenderDropdownField(
	formState: State<IUserDataFormState>,
	onGenderChange: (String) -> Unit = {}
) {
	var isGenderDropdownExpanded by rememberSaveable { mutableStateOf(false) }
	val genders: Map<String, String> = mapOf(
		Gender.MALE.name to stringResource(id = R.string.gender_item_male),
		Gender.FEMALE.name to stringResource(id = R.string.gender_item_female),
		Gender.UNKNOWN.name to stringResource(id = R.string.gender_item_unknown)
	)

	ExposedDropdownMenuBox(
		expanded = isGenderDropdownExpanded,
		onExpandedChange = { isGenderDropdownExpanded = !isGenderDropdownExpanded },
	) {
		OutlinedTextField(
			value = genders[formState.value.gender.name]
				?: stringResource(id = R.string.gender_item_unknown),
			onValueChange = onGenderChange,
			label = { Text(text = stringResource(R.string.dropdown_label_gender)) },
			readOnly = true,
			trailingIcon = {
				ExposedDropdownMenuDefaults.TrailingIcon(expanded = isGenderDropdownExpanded)
			},
			modifier = Modifier
				.menuAnchor(MenuAnchorType.PrimaryEditable)
				.fillMaxWidth()
		)

		ExposedDropdownMenu(
			expanded = isGenderDropdownExpanded,
			onDismissRequest = { isGenderDropdownExpanded = false }
		) {
			genders.forEach { (k, v) ->
				DropdownMenuItem(
					text = { Text(text = v) },
					onClick = {
						onGenderChange(k)
						isGenderDropdownExpanded = false
					}
				)
			}
		}
	}
}