package com.soundhub.ui.components.bars.top

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.MoreVert
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.soundhub.ui.components.menu.ChatTopBarMenu
import com.soundhub.utils.Constants
import com.soundhub.Route
import com.soundhub.UiStateDispatcher
import com.soundhub.ui.components.buttons.SearchButton
import com.soundhub.ui.components.fields.TransparentSearchTextField
import com.soundhub.ui.postline.components.PostlineNotificationTopBarButton

@Composable
fun TopBarActions(
    navController: NavHostController,
    uiStateDispatcher: UiStateDispatcher = hiltViewModel()
) {
    val currentBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = currentBackStackEntry?.destination?.route
    val isMenuExpanded = rememberSaveable { mutableStateOf(false) }
    val uiState by uiStateDispatcher.uiState.collectAsState()

    val isSearchBarActive = uiState.isSearchBarActive
    val searchBarText = uiState.searchBarText

    when (currentRoute) {
        Route.EditUserData.route -> {
            IconButton(onClick = {
                /* TODO: implement logic for saving changes */
                navController.popBackStack()
            }) { Icon(imageVector = Icons.Rounded.Check, contentDescription = "save_data" ) }
        }

        Route.Postline.route -> PostlineNotificationTopBarButton(navController)
        in listOf(Route.Music.route, Route.Messenger.route, Route.FriendList.route) -> {
            if (isSearchBarActive)
                TransparentSearchTextField(
                    value = searchBarText,
                    onValueChange = uiStateDispatcher::updateSearchBarText,
                    uiStateDispatcher = uiStateDispatcher
                )
            else SearchButton(uiStateDispatcher)
        }

        Route.Messenger.Chat().route -> {
            IconButton(onClick = { isMenuExpanded.value = !isMenuExpanded.value }) {
                Icon(imageVector = Icons.Rounded.MoreVert, contentDescription = "options")
            }

            ChatTopBarMenu(
                menuState = isMenuExpanded,
                navController = navController,
                chatId = currentBackStackEntry?.arguments?.getString(Constants.CHAT_NAV_ARG)
            )
        }

        else -> {}
    }
}