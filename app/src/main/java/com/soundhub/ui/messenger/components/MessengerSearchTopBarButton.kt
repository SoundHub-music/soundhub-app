package com.soundhub.ui.messenger.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable

@Composable
fun MessengerSearchTopBarButton() {
    IconButton(onClick = { /* TODO: write logic for searching chats */ }) {
        Icon(Icons.Rounded.Search, contentDescription = null)
    }
}