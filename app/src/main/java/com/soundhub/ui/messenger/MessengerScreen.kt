package com.soundhub.ui.messenger

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.soundhub.UiStateDispatcher
import com.soundhub.data.model.Chat
import com.soundhub.data.model.User
import com.soundhub.ui.authentication.AuthenticationViewModel
import com.soundhub.ui.components.containers.ContentContainer
import com.soundhub.ui.messenger.components.ChatList
import com.soundhub.utils.Constants

@Composable
fun MessengerScreen(
    navController: NavHostController,
    uiStateDispatcher: UiStateDispatcher = hiltViewModel(),
    authenticationViewModel: AuthenticationViewModel = hiltViewModel()
) {
    val authorizedUser: User? = authenticationViewModel.userInstance.collectAsState().value
    val chats = listOf(
        Chat(
            lastMessage = "last message",
            participants = listOf(
                User(
                    firstName = "Alexey",
                    lastName = "Zaycev"
                ),
                authorizedUser
            )
        ),

        Chat(
            lastMessage = "last message looooooooooooooooooooooooooooooooooooooooooooooooooooooooooong",
            participants = listOf(
                User(
                    firstName = "Nikolay",
                    lastName = "Shein"
                ),
                authorizedUser
            )
        ),

        Chat(
            lastMessage = "last message",
            unreadMessageCount = 2,
            participants = listOf(
                User(
                    firstName = "Gennadiy",
                    lastName = "Bukin"
                ),
                authorizedUser
            )
        )
    )

    val searchBarText = uiStateDispatcher.uiState.collectAsState().value.searchBarText
    var filteredChats by rememberSaveable { mutableStateOf(chats) }

    LaunchedEffect(key1 = searchBarText) {
        Log.d(Constants.LOG_SEARCH_TEXT, searchBarText)
        filteredChats = if (searchBarText.isNotEmpty()) {
            chats.filter {
                val otherUser = it.participants.first { user -> user?.id != authorizedUser?.id }
                otherUser?.firstName?.lowercase()?.contains(searchBarText.lowercase()) == true ||
                        otherUser?.lastName?.lowercase()?.contains(searchBarText.lowercase()) == true
            }
        } else chats
    }

    ContentContainer {
        ChatList(
            chats = filteredChats,
            navController = navController
        )
    }
}