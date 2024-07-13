package com.soundhub.ui.settings.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.net.toUri
import com.soundhub.data.model.User
import com.soundhub.ui.authentication.AuthenticationViewModel
import com.soundhub.ui.components.avatar.CircularAvatar
import com.soundhub.data.states.UiState
import com.soundhub.ui.viewmodels.UiStateDispatcher

@Composable
internal fun UserCardSettings(
    authViewModel: AuthenticationViewModel,
    uiStateDispatcher: UiStateDispatcher
) {
    val uiState: UiState by uiStateDispatcher.uiState.collectAsState(initial = UiState())
    val authorizedUser: User? = uiState.authorizedUser
    val userFullName: String = "${authorizedUser?.firstName} ${authorizedUser?.lastName}".trim()

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 15.dp)
    ) {
       Row(
           horizontalArrangement = Arrangement.SpaceBetween,
           verticalAlignment = Alignment.CenterVertically,
           modifier = Modifier.fillMaxWidth()
       ) {
           Row(
               verticalAlignment = Alignment.CenterVertically,
               horizontalArrangement = Arrangement.spacedBy(10.dp)
           ) {
               CircularAvatar(
                   modifier = Modifier.size(64.dp),
                   imageUri = authorizedUser?.avatarUrl?.toUri()
               )
               Text(
                   text = userFullName,
                   fontWeight = FontWeight.Bold,
                   fontSize = 18.sp,
                   lineHeight = 32.sp
               )
           }
            LogoutButton(authViewModel)
       }
    }
}