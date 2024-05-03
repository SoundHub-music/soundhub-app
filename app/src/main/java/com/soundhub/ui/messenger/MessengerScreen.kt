package com.soundhub.ui.messenger

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.navigation.NavHostController
import com.soundhub.Route
import com.soundhub.ui.viewmodels.UiStateDispatcher
import com.soundhub.data.model.Chat
import com.soundhub.data.model.User
import com.soundhub.ui.components.containers.ContentContainer
import com.soundhub.ui.messenger.components.ChatList
import com.soundhub.ui.states.UiState

@Composable
fun MessengerScreen(
    navController: NavHostController,
    uiStateDispatcher: UiStateDispatcher,
    messengerViewModel: MessengerViewModel
) {
    val messengerUiState: MessengerUiState by messengerViewModel
        .messengerUiState
        .collectAsState()

    val authorizedUserState: User? = messengerUiState.authorizedUser

    val uiState: UiState by uiStateDispatcher.uiState.collectAsState()

    val chats: List<Chat> = messengerUiState.chats
    val searchBarText: String = uiState.searchBarText
    var filteredChats: List<Chat> by rememberSaveable { mutableStateOf(chats) }

    LaunchedEffect(key1 = uiState.currentRoute) {
        if (uiState.currentRoute == Route.Messenger.route)
            messengerViewModel.loadChats()
    }

    LaunchedEffect(key1 = searchBarText) {
        Log.d("MessengerScreen", "searchbar text: $searchBarText")
        filteredChats = messengerViewModel.filterChats(
            chats = chats,
            searchBarText = searchBarText,
            authorizedUser = authorizedUserState
        )
    }

    LaunchedEffect(key1 = chats) {
        Log.d("MessengerScreen", "chats: $chats")
    }

    ContentContainer {
        if (filteredChats.isEmpty())
           EmptyMessengerScreen(navController = navController)
        else ChatList(
            chats = filteredChats,
            navController = navController,
            uiStateDispatcher = uiStateDispatcher
        )
    }
}