package com.soundhub.ui.components.menu

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AccountCircle
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.soundhub.R
import com.soundhub.Route
import com.soundhub.data.model.User
import com.soundhub.ui.messenger.chat.ChatUiState
import com.soundhub.ui.messenger.chat.ChatViewModel

@Composable
fun ChatTopBarDropdownMenu(
    menuState: MutableState<Boolean>,
    chatViewModel: ChatViewModel,
    chatState: ChatUiState,
    navController: NavHostController,
) {
    val profileErrorMessage: String = stringResource(id = R.string.toast_user_profile_error)
    val context = LocalContext.current

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
                    Icon(imageVector = Icons.Rounded.AccountCircle, contentDescription = null)
                    Text(text = stringResource(id = R.string.chat_menu_open_profile))
                }
            },
            onClick = { onOpenProfileClick(
                interlocutor = chatState.interlocutor,
                navController = navController,
                context = context,
                errorMessage = profileErrorMessage
            ) }
        )

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
            onClick = { chatState.chat?.id?.let { chatViewModel.deleteChat(it) } }
        )
    }
}


private fun onOpenProfileClick(
    interlocutor: User?,
    navController: NavHostController,
    context: Context,
    errorMessage: String
) {
    if (interlocutor != null)
        navController
            .navigate(Route.Profile
                .getStringRouteWithNavArg(interlocutor.id.toString()))
    else Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
}