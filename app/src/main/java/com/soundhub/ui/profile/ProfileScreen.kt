package com.soundhub.ui.profile

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.soundhub.data.model.User
import com.soundhub.ui.components.loaders.CircleLoader
import com.soundhub.ui.profile.components.UserProfileContainer
import com.soundhub.ui.profile.components.sections.avatar.UserProfileAvatar
import com.soundhub.ui.states.UiState
import com.soundhub.ui.viewmodels.UiStateDispatcher
import java.util.UUID
import kotlin.math.floor

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    navController: NavHostController,
    userId: UUID?,
    uiStateDispatcher: UiStateDispatcher,
    profileViewModel: ProfileViewModel,
) {
    val configuration = LocalConfiguration.current
    val uiState: UiState by uiStateDispatcher.uiState.collectAsState(initial = UiState())
    val authorizedUser: User? = uiState.authorizedUser
    val profileUiState by profileViewModel.profileUiState.collectAsState()
    val profileOwner: User? = profileUiState.profileOwner
    val isLoading: Boolean = remember(profileOwner) { profileOwner == null }

    val scaffoldState: BottomSheetScaffoldState = rememberBottomSheetScaffoldState(
        bottomSheetState = rememberStandardBottomSheetState(
            skipHiddenState = true,
            initialValue = SheetValue.PartiallyExpanded
        )
    )

    val bottomSheetState = remember(scaffoldState) { scaffoldState.bottomSheetState }
    val defaultBottomSheetShape = BottomSheetDefaults.ExpandedShape
    val hiddenBottomSheetShape = BottomSheetDefaults.HiddenShape
    var bottomSheetShape by remember { mutableStateOf(defaultBottomSheetShape) }

    val partiallyExpandedSheetHeight: Int by remember {
        derivedStateOf { floor(configuration.screenHeightDp / 2.0).toInt() }
    }


    LaunchedEffect(key1 = userId) {
        userId?.let { userId -> profileViewModel.loadProfileOwner(userId) }
    }

    LaunchedEffect(key1 = authorizedUser) {
        Log.d("ProfileScreen", "favorite artist ids: ${authorizedUser}")
    }

    LaunchedEffect(scaffoldState.bottomSheetState) {
        // Ensure the bottom sheet stays expanded
        snapshotFlow { scaffoldState.bottomSheetState.currentValue }
            .collect { currentValue ->
                if (currentValue == SheetValue.Expanded) {
                    bottomSheetShape = hiddenBottomSheetShape
                }
            }
    }

    BottomSheetScaffold(
        scaffoldState = scaffoldState,
        sheetContainerColor = MaterialTheme.colorScheme.primaryContainer,
        sheetContent = {
            UserProfileContainer(
                navController = navController,
                uiStateDispatcher = uiStateDispatcher,
                profileViewModel = profileViewModel
            )
        },
        sheetShape = if (bottomSheetState.currentValue == SheetValue.Expanded)
            BottomSheetDefaults.HiddenShape
            else BottomSheetDefaults.ExpandedShape,
        sheetPeekHeight = partiallyExpandedSheetHeight.dp
    ) {
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .background(MaterialTheme.colorScheme.background),
            verticalArrangement = Arrangement.spacedBy((-30).dp)
        ) {
            if (isLoading) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    CircleLoader(
                        modifier = Modifier.size(72.dp),
                        strokeWidth = 6.dp
                    )
                }
            } else {
                UserProfileAvatar(
                    navController = navController,
                    uiStateDispatcher = uiStateDispatcher,
                    profileViewModel = profileViewModel
                )
            }
        }
    }
}
