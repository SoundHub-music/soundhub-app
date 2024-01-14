package com.soundhub.ui.music

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import com.soundhub.ui.components.AppContainer
import com.soundhub.ui.components.ContentContainer
import com.soundhub.utils.UiEvent

@Composable
fun MusicScreen(
    viewModel: MusicViewModel = hiltViewModel(),
    onNavigate: (String) -> Unit
) {
    ContentContainer {
        Text(text = "This is a music page")
    }
}