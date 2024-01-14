package com.soundhub.ui.authentication.components

import android.content.Context
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.soundhub.R
import com.soundhub.ui.authentication.AuthenticationViewModel
import com.soundhub.ui.authentication.state.AuthValidationState

@Composable
fun AuthForm(
    isBottomSheetHidden: Boolean,
    authViewModel: AuthenticationViewModel = hiltViewModel()
) {
    val density: Density = LocalDensity.current
    val context: Context = LocalContext.current
    val authValidationState: AuthValidationState = authViewModel.authValidationState
    var buttonFormText: String = stringResource(id = R.string.auth_button_login_name)

    // if bottom sheet is hidden typed data is deleted
    if (isBottomSheetHidden) authViewModel.resetState()
    if (!authValidationState.isRegisterForm) authViewModel.resetRepeatedPassword()

    // it works every time when isRegisterForm variable is changed
    LaunchedEffect(key1 = authValidationState.isRegisterForm) {
        buttonFormText = if (authValidationState.isRegisterForm)
            context.getString(R.string.auth_button_register_name)
        else context.getString(R.string.auth_button_login_name)
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 30.dp, end = 30.dp, bottom = 20.dp),
        verticalArrangement = Arrangement.spacedBy(5.dp)
    ) {

        // email field
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = authValidationState.email,
            singleLine = true,
            onValueChange = { value -> authViewModel.onEmailTextFieldChange(value) },
            label = { Text("Email") },
            isError = !authValidationState.isEmailValid,
            supportingText = {
                if (!authValidationState.isEmailValid)
                    Text(
                        text = stringResource(id = R.string.invalid_email),
                        color = MaterialTheme.colorScheme.error
                    )
            },
            placeholder = { Text(stringResource(R.string.email_placeholder)) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
        )


        // password field
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = authValidationState.password,
            singleLine = true,
            onValueChange = {value -> authViewModel.onPasswordTextFieldChange(value)
            },
            label = { Text(stringResource(id = R.string.password_label)) },
            isError = !authValidationState.isPasswordValid || !authValidationState.arePasswordsEqual,
            visualTransformation = PasswordVisualTransformation(),
            placeholder = { Text(stringResource(R.string.password_placeholder)) },
            supportingText = {
                ErrorPasswordFieldColumn(authValidationState)
            }
        )

        // repeat password field
        AnimatedVisibility(
            visible = authValidationState.isRegisterForm,
            enter = slideInVertically(initialOffsetY = { with(density) { -40.dp.roundToPx() } }) +
                    expandVertically(expandFrom = Alignment.Top) +
                    fadeIn(initialAlpha = 0.3f),
            exit = slideOutVertically(targetOffsetY = { with(density) { -40.dp.roundToPx() } }) +
                    shrinkVertically(shrinkTowards = Alignment.Top) +
                    fadeOut()
        ) {
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = authValidationState.repeatedPassword ?: "",
                singleLine = true,
                onValueChange = { value -> authViewModel.onRepeatedPasswordTextFieldChange(value) },
                label = { Text(stringResource(id = R.string.repeat_password_label)) },
                isError = !authValidationState.isPasswordValid || !authValidationState.arePasswordsEqual,
                visualTransformation = PasswordVisualTransformation(),
                placeholder = { Text(stringResource(R.string.password_placeholder)) },
                supportingText = {
                    ErrorPasswordFieldColumn(authValidationState)
                }
            )
        }

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Switch(
                checked = authValidationState.isRegisterForm,
                onCheckedChange = { state -> authViewModel.onAuthTypeSwitchChange(state) }
            )
            Text(
                text = stringResource(R.string.get_account_switch_label),
                fontWeight = FontWeight.Bold
            )
        }
        FilledTonalButton(
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            shape = RoundedCornerShape(5.dp),
            colors = ButtonDefaults.buttonColors(),
            onClick = { authViewModel.onFormButtonClick() },
            enabled = authViewModel.validateForm()
        ) { Text(text = buttonFormText) }
    }
}

@Composable
fun ErrorPasswordFieldColumn(authValidationState: AuthValidationState) {
    Column {
        if (!authValidationState.isPasswordValid)
            Text(
                text = stringResource(id = R.string.invalid_password),
                color = MaterialTheme.colorScheme.error
            )
        if (!authValidationState.arePasswordsEqual && authValidationState.isRegisterForm)
            Text(
                text = stringResource(id = R.string.passwords_mismatch),
                color = MaterialTheme.colorScheme.error
            )
    }
}