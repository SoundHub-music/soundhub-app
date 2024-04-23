package com.soundhub.ui.authentication

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.BottomSheetScaffoldState
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.material3.rememberStandardBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.paint
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.soundhub.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import com.soundhub.ui.authentication.components.AuthForm
import com.soundhub.ui.authentication.components.LoginScreenLogo
import com.soundhub.ui.authentication.postregistration.RegistrationViewModel
import com.soundhub.ui.components.sheets.BottomSheet

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AuthenticationScreen(
    authViewModel: AuthenticationViewModel,
    registrationViewModel: RegistrationViewModel,
) {
    val backgroundImage: Painter = painterResource(R.drawable.login_page_background)
    val scaffoldState: BottomSheetScaffoldState = rememberBottomSheetScaffoldState(
        bottomSheetState = rememberStandardBottomSheetState(
            skipHiddenState = false,
            initialValue = SheetValue.Hidden
        )
    )

    LaunchedEffect(key1 = scaffoldState.bottomSheetState.currentValue) {
        Log.d(
            "AuthenticationScreen",
            "scaffold_state: ${scaffoldState.bottomSheetState.currentValue}"
        )
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .paint(painter = backgroundImage, contentScale = ContentScale.Crop),
    ) {
        Column(
            Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            LoginScreenLogo()
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.BottomCenter
            ) {
                StartButton(scaffoldState = scaffoldState)
                BottomSheet(
                    scaffoldState = scaffoldState,
                    sheetContent = {
                        AuthForm(
                            isBottomSheetHidden = !scaffoldState.bottomSheetState.isVisible,
                            registrationViewModel = registrationViewModel,
                            authViewModel = authViewModel
                        )
                    }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun StartButton(scaffoldState: BottomSheetScaffoldState) {
    val scope: CoroutineScope = rememberCoroutineScope()

    FilledTonalButton(
        modifier = Modifier
            .padding(bottom = 20.dp)
            .height(50.dp),
        colors = ButtonDefaults.buttonColors(),
        onClick = { scope.launch { scaffoldState.bottomSheetState.expand() } }
    ) {
        Text(
            text = stringResource(R.string.lets_start_login_btn),
            fontFamily = FontFamily(Font(R.font.nunito_bold)),
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onPrimary
        )
    }
}