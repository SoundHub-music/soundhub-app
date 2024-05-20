package com.soundhub.ui.components.bars.top

import androidx.compose.foundation.layout.Row
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material.icons.rounded.MoreVert
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.soundhub.utils.constants.Constants
import com.soundhub.Route
import com.soundhub.data.model.Message
import com.soundhub.data.model.User
import com.soundhub.ui.viewmodels.UiStateDispatcher
import com.soundhub.ui.components.buttons.SearchButton
import com.soundhub.ui.components.fields.TransparentSearchTextField
import com.soundhub.ui.components.menu.ChatTopBarDropdownMenu
import com.soundhub.ui.edit_profile.components.EditProfileTopBarButton
import com.soundhub.ui.messenger.chat.ChatUiState
import com.soundhub.ui.messenger.chat.ChatViewModel
import com.soundhub.ui.postline.components.PostlineNotificationTopBarButton
import com.soundhub.ui.states.UiState

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

    when (currentRoute) {
        Route.EditUserData.route ->
            EditProfileTopBarButton(
                uiStateDispatcher = uiStateDispatcher,
                navController = navController
            )

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
    chatViewModel: ChatViewModel,
    uiStateDispatcher: UiStateDispatcher
) {
    val isMenuExpanded: MutableState<Boolean> = rememberSaveable { mutableStateOf(false) }

    IconButton(onClick = { isMenuExpanded.value = !isMenuExpanded.value }) {
        Icon(imageVector = Icons.Rounded.MoreVert, contentDescription = "options")
    }

    ChatTopBarDropdownMenu(
        menuState = isMenuExpanded,
        navController = navController,
        chatState = chatState,
        chatViewModel = chatViewModel,
        uiStateDispatcher = uiStateDispatcher
    )
}


@Composable
internal fun ChatTopBarCheckMessagesActions(chatViewModel: ChatViewModel, uiStateDispatcher: UiStateDispatcher) {
    val uiState: UiState by uiStateDispatcher.uiState.collectAsState()
    val checkedMessages: List<Message> = uiState.checkedMessages
    val authorizedUser: User? = uiState.authorizedUser

    var hasOnlyOwnMessages by remember { mutableStateOf(false) }

    LaunchedEffect(key1 = checkedMessages, key2 = authorizedUser) {
        hasOnlyOwnMessages = checkedMessages.all { it.sender?.id == authorizedUser?.id }
    }

    Row {
        // TODO: add new message options
        if (hasOnlyOwnMessages) IconButton(
            onClick = { checkedMessages.forEach { message -> chatViewModel.deleteMessage(message) } }
        ) {
            Icon(imageVector = Icons.Rounded.Delete, contentDescription = "Delete message option")
        }
    }
}