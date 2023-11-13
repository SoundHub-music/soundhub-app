package com.soundhub.ui.authentication

import android.widget.Toast
import  androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.BottomSheetScaffoldState
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SheetState
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.paint
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.soundhub.R
import com.soundhub.ui.components.BottomSheet
import com.soundhub.ui.components.Container
import com.soundhub.utils.Validator
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import androidx.hilt.navigation.compose.hiltViewModel
import com.soundhub.data.model.User
import com.soundhub.utils.UiEvent

@OptIn(ExperimentalMaterial3Api::class)
//@Preview(name = "Login screen")
@Composable
fun LoginScreen(
    onNavigate: (UiEvent.Navigate) -> Unit,
    viewModel: AuthenticationViewModel = hiltViewModel()
) {
    val backgroundImage: Painter = painterResource(R.drawable.login_page_background)
    val scope: CoroutineScope = rememberCoroutineScope()
    val scaffoldState: BottomSheetScaffoldState = rememberBottomSheetScaffoldState(
        SheetState(initialValue = SheetValue.Hidden, skipPartiallyExpanded = false)
    )

    val context = LocalContext.current

    LaunchedEffect(key1 = true ) {
        viewModel.uiEvent.collect {
            event ->
            when(event) {
                is UiEvent.ShowToast -> Toast.makeText(context, event.message, Toast.LENGTH_SHORT).show()
                else -> Unit
            }
        }
    }

    Container(
        modifier = Modifier
            .fillMaxSize()
            .paint(
                backgroundImage, contentScale = ContentScale.Crop
            ),
    ) {
        Column(
            Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AppName()
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.BottomCenter
            ) {
                FilledTonalButton(
                    modifier = Modifier
                        .padding(bottom = 20.dp)
                        .height(50.dp),
                    colors = ButtonDefaults.buttonColors(),
                    onClick = {
                        scope.launch {
                            if (!scaffoldState.bottomSheetState.isVisible) {
                                scaffoldState.bottomSheetState.expand()
                            }
                            else scaffoldState.bottomSheetState.hide()
                        }}
                ) { Text(text = stringResource(R.string.lets_start_login_btn)) }

                BottomSheet(
                    scaffoldState = scaffoldState,
                    sheetContent = { LoginForm(!scaffoldState.bottomSheetState.isVisible, viewModel) }
                )
            }
        }
    }
}

@Preview(name = "App name with logo")
@Composable
fun AppName(modifier: Modifier = Modifier) {
    val appLogo: Painter = painterResource(R.drawable.soundhub_logo)
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(start = 16.dp, end = 16.dp, top = 30.dp, bottom = 30.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Image(painter = appLogo, contentDescription = "app logo")
        Spacer(modifier = Modifier.width(10.dp))
        Text(
            text = stringResource(R.string.app_name),
            color = Color.White,
            fontWeight = FontWeight.ExtraBold,
            fontSize = 50.sp
        )
    }
}


@Composable
fun LoginForm(isBottomSheetHidden: Boolean, viewModel: AuthenticationViewModel) {
    var emailValue: String by rememberSaveable { mutableStateOf("") }
    var passwordValue: String by rememberSaveable { mutableStateOf("") }
    var repeatPasswordValue: String by rememberSaveable { mutableStateOf("") }

    var isRegisterModeEnabled: Boolean by remember { mutableStateOf(false) }
    val density: Density = LocalDensity.current
    
    var isEmailError: Boolean by rememberSaveable { mutableStateOf(false) }
    var isPasswordMatchError: Boolean by rememberSaveable { mutableStateOf(false) }
    var isPasswordInvalid: Boolean by rememberSaveable { mutableStateOf(false) }

    if (isBottomSheetHidden) {
        emailValue = ""
        passwordValue = ""
        repeatPasswordValue = ""
        isRegisterModeEnabled = false
    }

    if (!isRegisterModeEnabled) repeatPasswordValue = ""

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 30.dp, end = 30.dp, bottom = 20.dp),
        verticalArrangement = Arrangement.spacedBy(5.dp)
    ) {

        // email field
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = emailValue,
            singleLine = true,
            onValueChange = { value ->
                emailValue = value
                isEmailError = !Validator.validateEmail(emailValue)
            },
            label = { Text("Email") },
            isError = isEmailError,
            supportingText = {
                if (isEmailError)
                    Text(
                        text = stringResource(R.string.invalid_email),
                        color = MaterialTheme.colorScheme.error
                    )
            },
            placeholder = { Text(stringResource(R.string.email_placeholder)) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
        )


        // password field
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = passwordValue,
            singleLine = true,
            onValueChange = {
                value -> passwordValue = value
                if (isRegisterModeEnabled)
                    isPasswordMatchError = Validator.arePasswordsNotEquals(value, repeatPasswordValue)
                isPasswordInvalid = !Validator.validatePassword(value)
            },
            label = { Text("Пароль") },
            isError = isPasswordMatchError || isPasswordInvalid,
            supportingText = {
                Column {
                    if (isPasswordMatchError && isRegisterModeEnabled)
                        Text(
                            text = stringResource(R.string.passwords_mismatch),
                            color = MaterialTheme.colorScheme.error
                        )
                    if (isPasswordInvalid)
                        Text(
                            text = stringResource(R.string.invalid_password),
                            color = MaterialTheme.colorScheme.error
                        )
                }
            },
            placeholder = { Text(stringResource(R.string.password_placeholder)) }
        )

        // repeat password field
        AnimatedVisibility(
            visible = isRegisterModeEnabled,
            enter = slideInVertically(initialOffsetY = { with(density) { -40.dp.roundToPx() } }) +
                    expandVertically(expandFrom = Alignment.Top) +
                    fadeIn(initialAlpha = 0.3f),
            exit = slideOutVertically(targetOffsetY = { with(density) { -40.dp.roundToPx() } }) +
                    shrinkVertically(shrinkTowards = Alignment.Top) +
                    fadeOut()
        ) {
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = repeatPasswordValue,
                singleLine = true,
                onValueChange = {
                    value -> repeatPasswordValue = value
                    if (isRegisterModeEnabled)
                        isPasswordMatchError = Validator.arePasswordsNotEquals(passwordValue, repeatPasswordValue)
                },
                label = { Text("Повторите пароль") },
                isError = isPasswordMatchError || isPasswordInvalid,
                supportingText = {
                    Column {
                        if (isPasswordMatchError && isRegisterModeEnabled)
                            Text(
                                text = stringResource(R.string.passwords_mismatch),
                                color = MaterialTheme.colorScheme.error
                            )
                        if (isPasswordInvalid)
                            Text(
                                text = stringResource(R.string.invalid_password),
                                color = MaterialTheme.colorScheme.error
                            )
                    }
                },
                placeholder = { Text(stringResource(R.string.password_placeholder)) },
            )
        }

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Switch(
                checked = isRegisterModeEnabled,
                onCheckedChange = { state -> isRegisterModeEnabled = state }
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
            onClick = {
                if (isRegisterModeEnabled) {
                    viewModel.onEvent(AuthenticationEvent.OnRegister(User(emailValue, passwordValue)))
                }
                else {
                    viewModel.onEvent(AuthenticationEvent.OnLogin(emailValue, passwordValue))
                }
            },
            enabled = Validator.validateAuthForm(
                emailValue,
                passwordValue,
                if (isRegisterModeEnabled) repeatPasswordValue else null)
        ) {
            Text(text = stringResource(
                id = if (isRegisterModeEnabled) R.string.auth_button_register_name
                else R.string.auth_button_login_name
            ))
        }
    }
}

