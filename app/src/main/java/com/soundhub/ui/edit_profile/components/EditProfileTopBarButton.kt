package com.soundhub.ui.edit_profile.components

import android.util.Log
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.soundhub.data.states.EmptyFormState
import com.soundhub.ui.components.forms.IUserDataFormState
import com.soundhub.ui.components.loaders.CircleLoader
import com.soundhub.ui.edit_profile.profile.EditUserProfileViewModel

@Composable
fun EditProfileTopBarButton(editUserProfileViewModel: EditUserProfileViewModel) {
    val formState: IUserDataFormState by editUserProfileViewModel
        .formState.collectAsState(initial = EmptyFormState())

    val isLoading by editUserProfileViewModel.isLoading.collectAsState()
    var hasChanges: Boolean by remember { mutableStateOf(false) }
    
    val activeIconColor = MaterialTheme.colorScheme.primary
    val iconColor: Color = remember(hasChanges) {
        if (hasChanges) activeIconColor else Color.Gray
    }

    LaunchedEffect(key1 = formState) {
        hasChanges = editUserProfileViewModel.hasStateChanges()
        Log.d("EditProfileTopBarButton", hasChanges.toString())
    }

    FilledTonalIconButton(
        onClick = { editUserProfileViewModel.onSaveChangesButtonClick() },
        shape = RoundedCornerShape(12.dp),
        enabled = hasChanges
    ) {
        if (isLoading)
            CircleLoader(
                modifier = Modifier.size(24.dp),
                strokeWidth = 3.dp
            )
        else Icon(
            imageVector = Icons.Rounded.Check,
            tint = iconColor,
            contentDescription = "save_data",
        )
    }
}