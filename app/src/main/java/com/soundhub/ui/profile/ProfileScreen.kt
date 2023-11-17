package com.soundhub.ui.profile

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.soundhub.ui.components.ContentContainer

@Composable
fun ProfileScreen(onNavigate: (String) -> Unit) {
    ContentContainer {
        Text(text = "This is a profile screen")
    }
}