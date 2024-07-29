package com.soundhub.ui.shared.containers

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.soundhub.R
import com.soundhub.data.enums.ApiStatus
import com.soundhub.ui.shared.loaders.CircleLoader

@Composable
fun FetchStatusContainer(
    modifier: Modifier = Modifier,
    @SuppressLint("ModifierParameter")
    loaderModifier: Modifier = Modifier.size(72.dp),
    errorTextStyle: TextStyle = TextStyle(
        fontSize = 20.sp,
        fontWeight = FontWeight.Bold,
        textAlign = TextAlign.Center
    ),
    status: ApiStatus,
    customErrorMessage: String? = null,
    handleLoading: Boolean = true,
    content: @Composable () -> Unit
) {
    val context: Context = LocalContext.current
    var isError: Boolean by rememberSaveable { mutableStateOf(false) }
    var isLoading: Boolean by rememberSaveable { mutableStateOf(true) }
    var messageScreenText: String by rememberSaveable { mutableStateOf("") }

    LaunchedEffect(key1 = status) {
        isError = status == ApiStatus.ERROR
        isLoading = status == ApiStatus.LOADING

        Log.d("MessengerChatList", "status: $status")

        messageScreenText = customErrorMessage ?:
            if (isError) context.getString(R.string.fetch_error_message) else ""
    }
    if (isError)
        Column(
            modifier = modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(0.8f)
            ) {
                if (isLoading && handleLoading) CircleLoader(modifier = loaderModifier)
                else if (messageScreenText.isNotEmpty())
                    Text(
                        text = messageScreenText,
                        style = errorTextStyle,
                    )
            }
        }
    else content()
}