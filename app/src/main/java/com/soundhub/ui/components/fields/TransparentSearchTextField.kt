package com.soundhub.ui.components.fields

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.soundhub.UiEventDispatcher
import com.soundhub.data.datastore.UserStore
import com.soundhub.utils.UiEvent

@Composable
fun TransparentSearchTextField(
    modifier: Modifier = Modifier,
    value: String = "",
    onValueChange: (String) -> Unit = {},
    uiEventDispatcher: UiEventDispatcher = hiltViewModel()
) {
    TextField(
        modifier = modifier
            .fillMaxWidth(),
//            .border(
//                border = BorderStroke(1.dp, Color.Gray),
//                shape = CircleShape
//            ),
        value = value,
        onValueChange = onValueChange,
        shape = CircleShape,
        textStyle = TextStyle(
            color = Color.White,
            fontSize = 16.sp
        ),
        leadingIcon = { Icon(imageVector = Icons.Rounded.Search, contentDescription = null) },
        trailingIcon = {
            IconButton(onClick = { uiEventDispatcher.sendUiEvent(UiEvent.SearchButtonClick) }) {
                Icon(imageVector = Icons.Rounded.Close, contentDescription = null)
            }
       },
        singleLine = true,
        colors = TextFieldDefaults.colors(
            focusedContainerColor = Color.Transparent,
            unfocusedContainerColor = Color.Transparent,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            disabledContainerColor = Color.Transparent,
        ),
    )
}

@Composable
@Preview(name = "SearchTextField", showBackground = true)
fun SearchTextFieldPreview() {
    val context = LocalContext.current
    var text by remember { mutableStateOf("") }

    TransparentSearchTextField(
        onValueChange = { text = it },
        value = text,
        uiEventDispatcher = UiEventDispatcher(UserStore(context))
    )
}