package com.soundhub.ui.profile

import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.hilt.navigation.compose.hiltViewModel
import com.soundhub.ui.authentication.AuthenticationViewModel
import com.soundhub.ui.components.ContentContainer
import com.soundhub.ui.mainActivity.MainViewModel
import com.soundhub.utils.UiEvent

@Composable
fun ProfileScreen(onNavigate: (String) -> Unit) {
    val authViewModel: AuthenticationViewModel = hiltViewModel()
    val mainViewModel: MainViewModel = hiltViewModel()

    val currentRoute = mainViewModel.currentRoute.collectAsState()

    LaunchedEffect(mainViewModel.uiEvent) {
        mainViewModel.uiEvent.collect {
            when (it) {
                is UiEvent.Navigate -> onNavigate(it.route.route)
                else -> Unit
            }
        }
    }
    
    ContentContainer {
        Button(onClick = { authViewModel.onLogoutButtonClick() }) {
            Text(text = "Выйти")
        }
    }
}