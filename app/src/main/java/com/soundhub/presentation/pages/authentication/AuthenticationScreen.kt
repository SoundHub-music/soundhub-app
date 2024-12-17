package com.soundhub.presentation.pages.authentication

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.BottomSheetScaffoldState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SheetValue
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.material3.rememberStandardBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.paint
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import com.soundhub.R
import com.soundhub.presentation.pages.authentication.ui.buttons.StartButton
import com.soundhub.presentation.pages.authentication.ui.forms.AuthForm
import com.soundhub.presentation.pages.authentication.ui.forms.LoginScreenLogo
import com.soundhub.presentation.pages.registration.RegistrationViewModel
import com.soundhub.presentation.shared.sheets.BottomSheet

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