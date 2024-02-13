package com.soundhub.ui.components.fields

import android.app.DatePickerDialog
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.soundhub.R
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Composable
fun DatePicker(
    modifier: Modifier = Modifier,
    label: String = "",
    value: String = "",
    onValueChange: (String) -> Unit = {},
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    pattern: String = "yyyy-MM-dd",
    isError: Boolean = false,
    supportingText: @Composable () -> Unit = {}
) {
    var currentDate by rememberSaveable { mutableStateOf("") }
    val context = LocalContext.current
    val invalidBirthdayMessage = stringResource(id = R.string.userform_invalid_birthday_error_message)

    val formatter = DateTimeFormatter.ofPattern(pattern)
    val date = if (value.isNotEmpty()) LocalDate.parse(value, formatter) else LocalDate.now().minusYears(14)
    val dialog = DatePickerDialog(
        context,
        { _, year, month, dayOfMonth ->
            val selectedDate = LocalDate.of(year, month + 1, dayOfMonth)
            if (selectedDate.plusYears(14) <= LocalDate.now()) {
                onValueChange(selectedDate.toString())
                currentDate = selectedDate.toString()
            }
            else Toast.makeText(context, invalidBirthdayMessage, Toast.LENGTH_SHORT).show()
        },
        date.year,
        date.monthValue - 1,
        date.dayOfMonth,
    )

    OutlinedTextField(
        label = { Text(text = label) },
        onValueChange = onValueChange,
        value = currentDate,
        readOnly = true,
        modifier = modifier
            .clickable { dialog.show() }
            .onFocusChanged {
                if (it.isFocused) dialog.show()
            },
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        supportingText = supportingText,
        isError = isError
    )
}

@Composable
@Preview
fun DatePickerPreview() {
    var value by remember { mutableStateOf("") }
    DatePicker(value = value, onValueChange = { value = it })
}