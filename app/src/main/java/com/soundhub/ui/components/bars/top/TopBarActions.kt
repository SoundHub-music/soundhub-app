package com.soundhub.ui.components.bars.top

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.MoreVert
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.soundhub.utils.constants.Constants
import com.soundhub.Route
import com.soundhub.ui.events.UiEvent
import com.soundhub.ui.viewmodels.UiStateDispatcher
import com.soundhub.ui.components.buttons.SearchButton
import com.soundhub.ui.components.fields.TransparentSearchTextField
import com.soundhub.ui.components.menu.ChatTopBarDropdownMenu
import com.soundhub.ui.messenger.chat.ChatUiState
import com.soundhub.ui.messenger.chat.ChatViewModel
import com.soundhub.ui.postline.components.PostlineNotificationTopBarButton
import com.soundhub.ui.states.UiState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun TopBarActions(
    navController: NavHostController,
    uiStateDispatcher: UiStateDispatcher
) {
    val currentBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute: String? = currentBackStackEntry?.destination?.route
    val uiState: UiState by uiStateDispatcher.uiState.collectAsState()

    val isSearchBarActive: Boolean = uiState.isSearchBarActive
    val searchBarText: String = uiState.searchBarText
    val coroutineScope: CoroutineScope = rememberCoroutineScope()

    when (currentRoute) {
        Route.EditUserData.route -> {
            IconButton(onClick = {
                coroutineScope.launch {
                    uiStateDispatcher.sendUiEvent(UiEvent.UpdateUserAction)
                    navController.popBackStack()
                }
            }) { Icon(imageVector = Icons.Rounded.Check, contentDescription = "save_data" ) }
        }

        Route.Postline.route -> PostlineNotificationTopBarButton(navController)
        in Constants.ROUTES_WITH_SEARCH_BAR -> {
            if (isSearchBarActive)
                TransparentSearchTextField(
                    value = searchBarText,
                    onValueChange = uiStateDispatcher::updateSearchBarText,
                    uiStateDispatcher = uiStateDispatcher
                )
            else SearchButton(uiStateDispatcher)
        }
        else -> {}
    }
}

@Composable
internal fun ChatTopBarActions(
    navController: NavHostController,
    chatState: ChatUiState,
    chatViewModel: ChatViewModel
) {
    val isMenuExpanded: MutableState<Boolean> = rememberSaveable { mutableStateOf(false) }

    IconButton(onClick = { isMenuExpanded.value = !isMenuExpanded.value }) {
        Icon(imageVector = Icons.Rounded.MoreVert, contentDescription = "options")
    }

    ChatTopBarDropdownMenu(
        menuState = isMenuExpanded,
        navController = navController,
        chatState = chatState,
        chatViewModel = chatViewModel
    )
}
