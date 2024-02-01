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
import androidx.compose.material3.SheetState
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberBottomSheetScaffoldState
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
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.soundhub.R
import com.soundhub.ui.components.BottomSheet
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import com.soundhub.ui.authentication.components.AuthForm
import com.soundhub.ui.authentication.components.LoginScreenLogo
import com.soundhub.ui.components.onBottomSheetButtonClick

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AuthenticationScreen(authViewModel: AuthenticationViewModel = hiltViewModel()) {
    val backgroundImage: Painter = painterResource(R.drawable.login_page_background)
    val scope: CoroutineScope = rememberCoroutineScope()
    val scaffoldState: BottomSheetScaffoldState = rememberBottomSheetScaffoldState(
        SheetState(initialValue = SheetValue.Hidden, skipPartiallyExpanded = false)
    )

    LaunchedEffect(scaffoldState) {
        Log.d("scaffold_state", scaffoldState.bottomSheetState.currentValue.toString())
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .paint(backgroundImage, contentScale = ContentScale.Crop),
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
                FilledTonalButton(
                    modifier = Modifier
                        .padding(bottom = 20.dp)
                        .height(50.dp),
                    colors = ButtonDefaults.buttonColors(),
                    onClick = { scope.launch { onBottomSheetButtonClick(scaffoldState)} }
                ) { Text(text = stringResource(R.string.lets_start_login_btn)) }

                BottomSheet(
                    scaffoldState = scaffoldState,
                    sheetContent = {
                        AuthForm(
                            isBottomSheetHidden = !scaffoldState.bottomSheetState.isVisible,
                            authViewModel = authViewModel
                        )
                    }
                )
            }
        }
    }
}