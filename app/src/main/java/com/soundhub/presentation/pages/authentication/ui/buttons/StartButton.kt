package com.soundhub.presentation.pages.authentication.ui.buttons

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.BottomSheetScaffoldState
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.soundhub.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun StartButton(scaffoldState: BottomSheetScaffoldState) {
	val scope: CoroutineScope = rememberCoroutineScope()

	FilledTonalButton(
		modifier = Modifier
			.padding(bottom = 20.dp)
			.height(50.dp),
		colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
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