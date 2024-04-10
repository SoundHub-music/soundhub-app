package com.soundhub.ui.settings.components

import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ExitToApp
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.soundhub.ui.authentication.AuthenticationViewModel

@Composable
internal fun LogoutButton(authViewModel: AuthenticationViewModel) {
    IconButton(onClick = { authViewModel.logout() }, modifier = Modifier.size(48.dp)) {
        Icon(
            imageVector = Icons.AutoMirrored.Rounded.ExitToApp,
            contentDescription = "logout button",
            tint = MaterialTheme.colorScheme.error,
            modifier = Modifier.size(28.dp)
        )
    }
}