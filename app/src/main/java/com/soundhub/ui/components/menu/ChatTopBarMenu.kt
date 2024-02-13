package com.soundhub.ui.components.menu

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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.soundhub.R
import com.soundhub.utils.Route

@Composable
fun ChatTopBarMenu(
    menuState: MutableState<Boolean>,
    chatId: String?,
    navController: NavHostController
) {
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
            onClick = {
                /* TODO: finish the logic */
                navController.navigate("${Route.Profile}/$chatId")
            }
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
            onClick = { /* TODO: make search message logic */ }
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
            onClick = { /* TODO: make delete history logic */ }
        )
    }
}
