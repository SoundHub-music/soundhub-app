package com.soundhub.ui.messenger

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.soundhub.ui.components.ContentContainer

@Composable
fun MessengerScreen(onNavigate: (String) -> Unit) {
    ContentContainer {
        Text(text = "This is a messenger screen")
    }
}