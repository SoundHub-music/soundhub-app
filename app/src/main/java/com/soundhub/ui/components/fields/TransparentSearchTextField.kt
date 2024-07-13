package com.soundhub.ui.components.fields

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.soundhub.data.datastore.UserSettingsStore
import com.soundhub.ui.viewmodels.UiStateDispatcher
import com.soundhub.ui.events.UiEvent
import com.soundhub.ui.states.UiState
import kotlinx.coroutines.launch

@Composable
fun TransparentSearchTextField(
    modifier: Modifier = Modifier,
    value: String = "",
    onValueChange: (String) -> Unit = {},
    uiStateDispatcher: UiStateDispatcher 
) {
    val uiState: UiState by uiStateDispatcher.uiState.collectAsState(initial = UiState())
    val isSearchBarActive = uiState.isSearchBarActive

    val focusRequester = remember { FocusRequester() }

    LaunchedEffect(key1 = isSearchBarActive) {
        if (isSearchBarActive) focusRequester.requestFocus()
    }

    TextField(
        modifier = modifier
            .focusRequester(focusRequester)
            .fillMaxWidth(),
        value = value,
        onValueChange = onValueChange,
        shape = CircleShape,
        textStyle = TextStyle(
            color = MaterialTheme.colorScheme.onBackground,
            fontSize = 16.sp
        ),
        leadingIcon = {
            Icon(
                imageVector = Icons.Rounded.Search,
                contentDescription = "search icon"
            )
        },
        trailingIcon = {
            CloseSearchBarButton(uiStateDispatcher)
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
private fun CloseSearchBarButton(uiStateDispatcher: UiStateDispatcher) {
    val coroutineScope = rememberCoroutineScope()

    IconButton(onClick = {
        coroutineScope.launch { uiStateDispatcher.sendUiEvent(UiEvent.SearchButtonClick) }
    }) {
        Icon(
            imageVector = Icons.Rounded.Close,
            contentDescription = "close search bar button"
        )
    }
}

@Composable
@Preview(name = "SearchTextField", showBackground = true)
private fun SearchTextFieldPreview() {
    var text by remember { mutableStateOf("") }
    val context = LocalContext.current

    TransparentSearchTextField(
        onValueChange = { text = it },
        value = text,
        uiStateDispatcher = UiStateDispatcher(UserSettingsStore(context))
    )
}