package com.soundhub.ui.postline

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.soundhub.ui.components.ContentContainer

@Composable
fun PostLineScreen(
    modifier: Modifier = Modifier,
    viewModel: PostlineViewModel = hiltViewModel(),
    onNavigate: (String) -> Unit,
) {
    ContentContainer {
        Text("postline")
    }
}