package com.soundhub.ui.components.bars.top

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.soundhub.UiEventDispatcher
import com.soundhub.utils.Constants
import com.soundhub.utils.Route

@Composable
internal fun TopAppBarBuilder(
    currentRoute: String?,
    topBarTitle: String?,
    navController: NavHostController,
    uiEventDispatcher: UiEventDispatcher = hiltViewModel(),
) {

    when (Regex(Constants.DYNAMIC_PART_ROUTE).replace(currentRoute ?: "", "")) {
        Route.Messenger.Chat.staticDestination -> { ChatTopAppBar(navController) }
        in Constants.ROUTES_WITH_NATIVE_TOP_APP_BAR -> { DefaultTopAppBar(topBarTitle, navController) }
        else -> {
            // custom top app bar
            CustomTopAppBar(
                topBarTitle = topBarTitle,
                currentRoute = currentRoute,
                navController = navController,
                uiEventDispatcher = uiEventDispatcher
            )
        }
    }
}