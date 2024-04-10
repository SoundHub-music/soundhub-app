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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.soundhub.R
import com.soundhub.ui.authentication.states.UserState
import kotlinx.coroutines.launch

@Composable
fun CreatePostScreen(
    createPostViewModel: CreatePostViewModel = hiltViewModel(),
    user: UserState?,
) {
    val postState by createPostViewModel.postState.collectAsState()
    val coroutineScope = rememberCoroutineScope()

    Box(
        contentAlignment = Alignment.BottomEnd
    ) {
        OutlinedTextField(
            modifier = Modifier.fillMaxSize(),
            value = postState.content,
            onValueChange = { createPostViewModel.setContent(it) },
            placeholder = {
                Text(
                    text = stringResource(id = R.string.create_post_field_placeholder),
                    fontSize = 20.sp
                )
            }
        )


        FloatingActionButton(
            containerColor = MaterialTheme.colorScheme.secondaryContainer,
            modifier = Modifier.padding(10.dp),
            onClick = {
                coroutineScope.launch {
                    createPostViewModel.createPost(author = user?.current,)
                }
            }
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Rounded.Send,
                contentDescription = "send post"
            )
        }
    }
}