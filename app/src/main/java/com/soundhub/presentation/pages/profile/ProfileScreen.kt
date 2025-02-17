package com.soundhub.presentation.pages.profile

import android.util.Log
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.NavHostController
import com.soundhub.domain.states.UiState
import com.soundhub.presentation.layout.ProfileLayout
import com.soundhub.presentation.pages.profile.ui.UserProfileContainer
import com.soundhub.presentation.viewmodels.UiStateDispatcher
import java.util.UUID

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
	navController: NavHostController,
	userId: UUID?,
	uiStateDispatcher: UiStateDispatcher,
	profileViewModel: ProfileViewModel,
) {
//	val configuration = LocalConfiguration.current
	val uiState by uiStateDispatcher.uiState.collectAsState(initial = UiState())

//	val scaffoldState: BottomSheetScaffoldState = rememberBottomSheetScaffoldState(
//		bottomSheetState = rememberStandardBottomSheetState(
//			skipHiddenState = true,
//			initialValue = SheetValue.PartiallyExpanded
//		)
//	)
//
//	val bottomSheetState = scaffoldState.bottomSheetState
//
//	val partiallyExpandedSheetHeight: Int by remember {
//		derivedStateOf { floor(configuration.screenHeightDp / 2.0).toInt() }
//	}
//
//	var targetCornerRadius by remember { mutableStateOf(30.dp) }
//	val animatedCornerRadius by animateDpAsState(
//		targetValue = targetCornerRadius,
//		label = "bottom sheet corner animation"
//	)

	LaunchedEffect(key1 = userId) {
		Log.d("ProfileScreen", "userId: $userId")
		userId?.let { profileViewModel.loadProfileOwner(it) }
	}

	// update profile owner if authorized user instance has been changed
	LaunchedEffect(key1 = uiState.authorizedUser) {
		if (userId != uiState.authorizedUser?.id)
			return@LaunchedEffect

		val authorizedUser = uiState.authorizedUser

		authorizedUser?.let {
			profileViewModel.loadProfileOwner(it.id)
		}
	}

//	// changing bottom sheet border radius
//	LaunchedEffect(bottomSheetState) {
//		snapshotFlow { bottomSheetState.targetValue }
//			.collect { sheetValue ->
//				targetCornerRadius = when (sheetValue) {
//					SheetValue.Expanded -> 0.dp
//					SheetValue.PartiallyExpanded -> 30.dp
//					else -> 30.dp
//				}
//			}
//	}

	ProfileLayout(
		navController = navController,
		profileViewModel = profileViewModel
	) {
		UserProfileContainer(
			navController = navController,
			uiStateDispatcher = uiStateDispatcher,
			profileViewModel = profileViewModel
		)
	}

//	BottomSheetScaffold(
//		scaffoldState = scaffoldState,
//		sheetContainerColor = MaterialTheme.colorScheme.primaryContainer,
//		sheetContent = {
//			UserProfileContainer(
//				navController = navController,
//				uiStateDispatcher = uiStateDispatcher,
//				profileViewModel = profileViewModel
//			)
//		},
//		sheetShape = RoundedCornerShape(
//			topStart = animatedCornerRadius,
//			topEnd = animatedCornerRadius
//		),
//		sheetPeekHeight = partiallyExpandedSheetHeight.dp
//	) {
//		Column(
//			modifier = Modifier
//				.fillMaxWidth()
//				.background(MaterialTheme.colorScheme.background),
//			verticalArrangement = Arrangement.spacedBy((-30).dp)
//		) {
//			UserProfileAvatar(
//				navController = navController,
//				profileViewModel = profileViewModel
//			)
//		}
//	}
}

