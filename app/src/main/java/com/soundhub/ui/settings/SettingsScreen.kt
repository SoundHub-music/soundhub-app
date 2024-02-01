package com.soundhub.ui.settings

import androidx.compose.foundation.layout.Box
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import com.soundhub.ui.authentication.AuthenticationViewModel

@Composable
fun SettingsScreen() {
    val authViewModel: AuthenticationViewModel = hiltViewModel()

    Box() {
       Button(onClick = { authViewModel.logout() }) {
           Text(text = "Выйти")
       }
    }
}