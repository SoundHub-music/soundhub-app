package com.soundhub.ui.components.bars.top

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.MoreVert
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.soundhub.ui.components.menu.ChatTopBarMenu
import com.soundhub.utils.Constants
import com.soundhub.utils.Route

@Composable
fun TopBarActions(navController: NavHostController) {
    val currentBackStackEntry = navController.currentBackStackEntryAsState().value
    val currentRoute = currentBackStackEntry?.destination?.route

    val dynamicPartRegex = Regex(Constants.DYNAMIC_PART_ROUTE)
    var isMenuExpanded = rememberSaveable {
        mutableStateOf(false)
    }

    when (dynamicPartRegex.replace(currentRoute ?: "", "")) {
        Route.EditUserData.route -> {
            IconButton(onClick = {
                /* TODO: write logic for saving changes */
                navController.popBackStack()
            }) {
                Icon(imageVector = Icons.Rounded.Check, contentDescription = "save_data" )
            }
        }

        Route.Messenger.Chat.staticDestination -> {
            IconButton(onClick = {
                isMenuExpanded.value = !isMenuExpanded.value
            }) {
                Icon(imageVector = Icons.Rounded.MoreVert, contentDescription = "options")
            }

            ChatTopBarMenu(state = isMenuExpanded)
        }
        else -> {}
    }
}