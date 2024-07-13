package com.soundhub.ui.components.menu

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.soundhub.R
import com.soundhub.data.states.ChatUiState
import com.soundhub.ui.chat.ChatViewModel
import com.soundhub.data.states.UiState
import com.soundhub.ui.viewmodels.UiStateDispatcher

@Composable
fun ChatTopBarDropdownMenu(
    menuState: MutableState<Boolean>,
    chatViewModel: ChatViewModel,
    chatState: ChatUiState,
    navController: NavHostController,
    uiStateDispatcher: UiStateDispatcher
) {
    val uiState: UiState by uiStateDispatcher.uiState.collectAsState(initial = UiState())
    val currentRoute: String? = uiState.currentRoute

    LaunchedEffect(key1 = currentRoute) {
        if (menuState.value)
            menuState.value = false
    }

    DropdownMenu(
        expanded = menuState.value,
        onDismissRequest = { menuState.value = false }
    ) {
        DropdownMenuItem(
            text = {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(5.dp)
                ) {
                    Icon(imageVector = Icons.Rounded.Search, contentDescription = null)
                    Text(text = stringResource(id = R.string.chat_menu_search))
                }
            },
            onClick = { /* TODO: implement search message logic */ }
        )

        DropdownMenuItem(
            text = {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(5.dp)
                ) {
                    Icon(
                        imageVector = Icons.Rounded.Delete,
                        contentDescription = null,

                        tint = MaterialTheme.colorScheme.error
                    )
                    Text(
                        text = stringResource(id = R.string.chat_menu_delete_history),
                        color = MaterialTheme.colorScheme.error
                    )
                }
            },
            onClick = {
                chatState.chat?.id?.let {
                    chatViewModel.deleteChat(it)
                    navController.popBackStack()
                }
            }
        )
    }
}