package com.soundhub.ui.edit_profile.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.navigation.NavHostController
import com.soundhub.ui.events.UiEvent
import com.soundhub.ui.viewmodels.UiStateDispatcher
import kotlinx.coroutines.launch

@Composable
fun EditProfileTopBarButton(
    uiStateDispatcher: UiStateDispatcher,
    navController: NavHostController
) {
    val coroutineScope = rememberCoroutineScope()

    IconButton(onClick = {
        coroutineScope.launch {
            uiStateDispatcher.sendUiEvent(UiEvent.UpdateUserAction)
            navController.popBackStack()
        }
    }) { Icon(imageVector = Icons.Rounded.Check, contentDescription = "save_data" ) }
}