package com.soundhub.ui.components.bars.top

import android.util.Log
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
    Log.d("current_route:TopAppBarBuilder", currentRoute.toString())
    Log.d("choose-genres", Route.Authentication.withNavArg)
    when (currentRoute) {
        in Constants.ROUTES_WITH_CUSTOM_TOP_APP_BAR -> {
            CustomTopAppBar(
                topBarTitle = topBarTitle,
                currentRoute = currentRoute,
                navController = navController,
                uiStateDispatcher = uiStateDispatcher
            )
        }
        in Constants.ROUTES_WITHOUT_TOP_APP_BAR -> {}
        Route.Messenger.Chat().route -> ChatTopAppBar(navController = navController)
        else -> { DefaultTopAppBar(topBarTitle, navController) }
    }
}