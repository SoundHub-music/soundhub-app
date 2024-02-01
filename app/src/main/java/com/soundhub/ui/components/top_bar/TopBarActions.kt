package com.soundhub.ui.components.top_bar

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import com.soundhub.utils.Route

@Composable
fun TopBarActions(currentRoute: String?, navController: NavHostController) {
    when (currentRoute) {
        Route.EditUserData.route -> {
            IconButton(onClick = {
                /* TODO: write logic for saving changes */
                navController.popBackStack()
            }) {
                Icon(imageVector = Icons.Rounded.Check, contentDescription = "complete_editing" )
            }
        }
        else -> {}
    }
}