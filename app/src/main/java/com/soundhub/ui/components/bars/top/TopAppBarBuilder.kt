package com.soundhub.ui.components.bars.top

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.soundhub.UiStateDispatcher
import com.soundhub.utils.Constants
import com.soundhub.Route

@Composable
internal fun TopAppBarBuilder(
    currentRoute: String?,
    topBarTitle: String?,
    navController: NavHostController,
    uiStateDispatcher: UiStateDispatcher = hiltViewModel(),
) {
    when (currentRoute) {
        in Constants.ROUTES_WITH_CUSTOM_TOP_APP_BAR -> {
            CustomTopAppBar(
                topBarTitle = topBarTitle,
                navController = navController,
                uiStateDispatcher = uiStateDispatcher
            )
        }
        Route.Messenger.Chat().route -> ChatTopAppBar(navController = navController)
        in Constants.ROUTES_WITHOUT_TOP_APP_BAR -> {}
        else -> { DefaultTopAppBar(topBarTitle, navController) }
    }
}