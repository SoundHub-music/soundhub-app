package com.soundhub.ui.components.avatar

import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.soundhub.utils.enums.ContentTypes

@Composable
fun AvatarPicker(
    modifier: Modifier = Modifier,
    imageUriState: MutableState<Uri?>,
    onImagePick: (Uri?) -> Unit
) {
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri -> onImagePick(uri) }

    LaunchedEffect(key1 = imageUriState.value) {
        Log.d("AvatarPicker", imageUriState.value?.scheme.toString())
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CircularAvatar(
            modifier = Modifier.size(150.dp),
            imageUri = imageUriState.value,
            onClick = { launcher.launch(ContentTypes.IMAGE_ALL.type) }
        )
    }
}

@Composable
@Preview
private fun AvatarPickerPreview() {
    val imageUri: MutableState<Uri?> = remember { mutableStateOf(null) }
    AvatarPicker(
        imageUriState = imageUri,
        onImagePick = { imageUri.value = it }
    )
}