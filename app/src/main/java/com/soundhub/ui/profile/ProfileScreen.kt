package com.soundhub.ui.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.soundhub.ui.authentication.AuthenticationViewModel
import com.soundhub.ui.profile.components.UserAvatar
import com.soundhub.ui.profile.components.ProfileBody

@Composable
fun ProfileScreen() {
    val authViewModel: AuthenticationViewModel = hiltViewModel()

    Column(
        modifier = Modifier
            .fillMaxHeight()
            .background(MaterialTheme.colorScheme.background),
        // negative indent provides an overlay of the content on the avatar
        verticalArrangement = Arrangement.spacedBy((-30).dp)
    ) {
        UserAvatar()
        ProfileBody()
    }
}