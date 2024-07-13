package com.soundhub.ui.chat.components.input_box

import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import com.soundhub.R

@Composable
fun AttachFileButton() {
    IconButton(onClick = { /* TODO: implement logic for file attaching */ }) {
        Icon(painter = painterResource(id = R.drawable.baseline_attach_file_24), contentDescription = null)
    }
}