package com.soundhub.ui.components.fields

import android.app.DatePickerDialog
import android.util.Log
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
import java.time.format.DateTimeParseException

@Composable
fun DatePicker(
    modifier: Modifier = Modifier,
    label: String = "",
    value: LocalDate?,
    onValueChange: (String) -> Unit = {},
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    isError: Boolean = false,
    supportingText: @Composable () -> Unit = {}
) {
    var stringDate: String by rememberSaveable { mutableStateOf("") }
    val context = LocalContext.current
    val invalidBirthdayMessage: String = stringResource(id = R.string.user_form_invalid_birthday_error_message)
    val defaultDate: LocalDate = LocalDate.now().minusYears(14)

    val dialog = DatePickerDialog(
        context,
        { _, year, month, dayOfMonth ->
            val selectedDate = LocalDate.of(year, month + 1, dayOfMonth)
            if (selectedDate.plusYears(14) <= LocalDate.now()) {
                onValueChange(selectedDate.toString())
                stringDate = selectedDate.toString()
            }
            else Toast.makeText(context, invalidBirthdayMessage, Toast.LENGTH_SHORT).show()
        },
        value?.year ?: defaultDate.year,
        value?.monthValue?.minus(1) ?: defaultDate.monthValue,
        value?.dayOfMonth ?: defaultDate.dayOfMonth,
    )

    OutlinedTextField(
        label = { Text(text = label) },
        onValueChange = onValueChange,
        value = value?.toString() ?: "",
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

private fun parseLocalDate(stringDate: String, pattern: String): LocalDate {
    val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern(pattern)
    return try {
        if (stringDate.isNotEmpty()) LocalDate.parse(stringDate, formatter)
        else LocalDate.now().minusYears(14)
    }
    catch (e: DateTimeParseException) {
        Log.e("DatePicker", e.stackTraceToString())
        LocalDate.now().minusYears(14)
    }
}

@Composable
@Preview
private fun DatePickerPreview() {
    var value by remember { mutableStateOf("") }
    DatePicker(value = LocalDate.now(), onValueChange = { value = it })
}