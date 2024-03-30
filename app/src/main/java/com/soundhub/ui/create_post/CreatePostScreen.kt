package com.soundhub.ui.create_post

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.Send
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.soundhub.R

@Composable
fun CreatePostScreen() {
    var postContent by remember { mutableStateOf("") }
    Box(
        contentAlignment = Alignment.BottomEnd
    ) {
        OutlinedTextField(
            modifier = Modifier.fillMaxSize(),
            value = postContent,
            onValueChange = { postContent = it },
            placeholder = {
                Text(
                    text = stringResource(
                        id = R.string.create_post_field_placeholder
                    ),
                    fontSize = 20.sp
                )
            }
        )


        FloatingActionButton(
            containerColor = MaterialTheme.colorScheme.secondaryContainer,
            modifier = Modifier.padding(10.dp),
            onClick = { /*TODO*/ }
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Rounded.Send,
                contentDescription = "send post"
            )
        }
    }
}