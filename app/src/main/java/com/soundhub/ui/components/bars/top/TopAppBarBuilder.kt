package com.soundhub.ui.components.bars.top

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import com.soundhub.ui.viewmodels.UiStateDispatcher
import com.soundhub.utils.Constants
import com.soundhub.Route
import com.soundhub.ui.messenger.chat.ChatViewModel

@Composable
internal fun TopAppBarBuilder(
    currentRoute: String?,
    topBarTitle: String?,
    navController: NavHostController,
    uiStateDispatcher: UiStateDispatcher,
    chatViewModel: ChatViewModel ,
) {
    when (currentRoute) {
        in Constants.ROUTES_WITH_CUSTOM_TOP_APP_BAR ->
            CustomTopAppBar(
                topBarTitle = topBarTitle,
                navController = navController,
                uiStateDispatcher = uiStateDispatcher
            )

        Route.Messenger.Chat().route -> ChatTopAppBar(
            navController = navController,
            chatViewModel = chatViewModel,
            uiStateDispatcher = uiStateDispatcher
        )
        in Constants.ROUTES_WITHOUT_TOP_APP_BAR -> {}
        else -> DefaultTopAppBar(
            topBarTitle = topBarTitle,
            navController = navController,
            uiStateDispatcher = uiStateDispatcher,
        )
    }
}