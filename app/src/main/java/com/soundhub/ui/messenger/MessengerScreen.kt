package com.soundhub.ui.messenger

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.soundhub.R
import com.soundhub.Route
import com.soundhub.UiStateDispatcher
import com.soundhub.data.model.Chat
import com.soundhub.data.model.User
import com.soundhub.ui.authentication.AuthenticationViewModel
import com.soundhub.ui.components.containers.ContentContainer
import com.soundhub.ui.messenger.components.ChatList
import com.soundhub.utils.Constants
import com.soundhub.utils.SearchUtils

@Composable
fun MessengerScreen(
    navController: NavHostController,
    uiStateDispatcher: UiStateDispatcher = hiltViewModel(),
    authViewModel: AuthenticationViewModel = hiltViewModel()
) {
    val authorizedUser: User? = authViewModel.userInstance.collectAsState().value
    val chats: List<Chat> = listOf(
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
    var filteredChats: List<Chat> by rememberSaveable { mutableStateOf(chats) }

    LaunchedEffect(key1 = searchBarText) {
        Log.d(Constants.LOG_SEARCH_TEXT, searchBarText)
        filteredChats = if (searchBarText.isNotEmpty()) {
            chats.filter {
                val otherUser: User? = it.participants.first { user -> user?.id != authorizedUser?.id }
               SearchUtils.compareWithUsername(otherUser, searchBarText)
            }
        } else chats
    }

    ContentContainer {
        if (chats.isEmpty())
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(
                    space = 10.dp,
                    alignment = Alignment.CenterVertically
                ),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = stringResource(id = R.string.empty_messenger_screen),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Light
                )
                OutlinedButton(
                    onClick = { navController.navigate(Route.FriendList.route) },
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Text(
                        text = stringResource(id = R.string.empty_messenger_screen_button),
                        fontSize = 16.sp
                    )
                }
            }
        else ChatList(
            chats = filteredChats,
            navController = navController
        )
    }
}