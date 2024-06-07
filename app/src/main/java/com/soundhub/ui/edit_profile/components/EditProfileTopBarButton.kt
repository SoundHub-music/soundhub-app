package com.soundhub.ui.edit_profile.components

import android.util.Log
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.navigation.NavHostController
import com.soundhub.ui.components.forms.EmptyFormState
import com.soundhub.ui.components.forms.IUserDataFormState
import com.soundhub.ui.edit_profile.profile.EditUserProfileViewModel
import kotlinx.coroutines.launch

@Composable
fun EditProfileTopBarButton(
    navController: NavHostController,
    editUserProfileViewModel: EditUserProfileViewModel
) {
    val coroutineScope = rememberCoroutineScope()
    val formState: IUserDataFormState by editUserProfileViewModel.formState.collectAsState(initial = EmptyFormState())
    var hasChanges: Boolean by remember { mutableStateOf(false) }

    LaunchedEffect(key1 = formState) {
        hasChanges = editUserProfileViewModel.hasStateChanges()
        Log.d("EditProfileTopBarButton", hasChanges.toString())
    }

    IconButton(
        onClick = {
        coroutineScope.launch {
            editUserProfileViewModel.updateUser()
            navController.popBackStack()
        }
    },
        enabled = hasChanges
    ) {
        Icon(
            imageVector = Icons.Rounded.Check,
            tint = MaterialTheme.colorScheme.primary,
            contentDescription = "save_data",
        )
    }
}