package com.soundhub.ui.shared.bars.top

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import com.soundhub.ui.pages.notifications.NotificationViewModel
import com.soundhub.ui.viewmodels.UiStateDispatcher
import com.soundhub.utils.constants.Constants

@Composable
internal fun TopAppBarBuilder(
    currentRoute: String?,
    topBarTitle: String?,
    navController: NavHostController,
    uiStateDispatcher: UiStateDispatcher,
    notificationViewModel: NotificationViewModel
) {
    when (currentRoute) {
        in Constants.ROUTES_WITH_CUSTOM_TOP_APP_BAR ->
            CustomTopAppBar(
                topBarTitle = topBarTitle,
                navController = navController,
                uiStateDispatcher = uiStateDispatcher,
                notificationViewModel = notificationViewModel
            )

        in (Constants.ROUTES_WITHOUT_TOP_APP_BAR + Constants.ROUTES_WITH_INNER_TOP_APP_BAR) -> {}
        else -> DefaultTopAppBar(
            topBarTitle = topBarTitle,
            navController = navController,
            uiStateDispatcher = uiStateDispatcher,
            notificationViewModel = notificationViewModel
        )
    }
}