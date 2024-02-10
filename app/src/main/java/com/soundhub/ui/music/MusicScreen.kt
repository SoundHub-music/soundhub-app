package com.soundhub.ui.music

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import com.soundhub.ui.components.containers.ContentContainer

@Composable
fun MusicScreen(
    viewModel: MusicViewModel = hiltViewModel(),
) {
    ContentContainer {
        Text(text = "This is a music page")
    }
}