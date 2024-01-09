package com.soundhub.ui.authentication

import android.widget.Toast
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.soundhub.R
import com.soundhub.ui.components.BottomSheet
import com.soundhub.ui.components.BoxContainer
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import androidx.hilt.navigation.compose.hiltViewModel
import com.soundhub.ui.authentication.components.AuthForm
import com.soundhub.ui.authentication.components.LoginScreenLogo
import com.soundhub.ui.mainActivity.MainViewModel
import com.soundhub.utils.UiEvent

@Composable
fun LoginScreen(
    onNavigate: (String) -> Unit,
) {
    val mainViewModel: MainViewModel = hiltViewModel()
    val context = LocalContext.current

    LaunchedEffect(mainViewModel.uiEvent) {
        mainViewModel.uiEvent.collect {
            event ->
            when(event) {
                is UiEvent.ShowToast -> Toast.makeText(context, event.message, Toast.LENGTH_SHORT).show()
                is UiEvent.Navigate -> onNavigate(event.route.route)
                else -> Unit
            }
        }
    }

    LoginScreenContent()
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreenContent() {
    val backgroundImage: Painter = painterResource(R.drawable.login_page_background)
    val scope: CoroutineScope = rememberCoroutineScope()
    val scaffoldState: BottomSheetScaffoldState = rememberBottomSheetScaffoldState(
        SheetState(initialValue = SheetValue.Hidden, skipPartiallyExpanded = false)
    )

    BoxContainer(
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
                    onClick = { scope.launch { onBottomSheetButtonClick(scaffoldState)}}
                ) { Text(text = stringResource(R.string.lets_start_login_btn)) }

                BottomSheet(
                    scaffoldState = scaffoldState,
                    sheetContent = { AuthForm(!scaffoldState.bottomSheetState.isVisible) }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
suspend fun onBottomSheetButtonClick(scaffoldState: BottomSheetScaffoldState) {
    if (!scaffoldState.bottomSheetState.isVisible)
        scaffoldState.bottomSheetState.expand()
    else scaffoldState.bottomSheetState.hide()
}