package com.soundhub.presentation.shared.containers

import android.content.Context
import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.soundhub.R
import com.soundhub.data.enums.ApiStatus
import com.soundhub.presentation.shared.loaders.CircleLoader
import kotlinx.coroutines.launch

val textStyle = TextStyle(
	fontSize = 20.sp,
	fontWeight = FontWeight.Bold,
	textAlign = TextAlign.Center
)

@Composable
fun FetchStatusContainer(
	modifier: Modifier = Modifier,
	loaderModifier: Modifier = Modifier.size(72.dp),
	emptyTextStyle: TextStyle = textStyle,
	errorTextStyle: TextStyle = textStyle,
	status: ApiStatus,
	emptyMessage: String? = null,
	errorMessage: String? = null,
	handleLoading: Boolean = true,
	isRefreshing: Boolean = false,
	onRefresh: suspend () -> Unit = {},
	content: @Composable () -> Unit
) {
	val context: Context = LocalContext.current

	var messageScreenText: String = rememberSaveable(status) {
		errorMessage ?: if (status.isError())
			context.getString(R.string.fetch_error_message)
		else ""
	}

	LaunchedEffect(key1 = isRefreshing) {
		Log.d("FetchStatusContainer", "isRefreshing: $isRefreshing")
	}

	LaunchedEffect(key1 = status) {
		Log.d("FetchStatusContainer", "status: $status")
	}

	when {
		status.isLoading() -> LoadingScreen(
			handleLoading = handleLoading,
			loaderModifier = loaderModifier
		)

		status.isError() -> ErrorScreen(
			modifier = modifier,
			text = messageScreenText,
			textStyle = errorTextStyle,
			onRefresh = onRefresh
		)

		emptyMessage.orEmpty().isNotEmpty() -> EmptyScreen(
			text = emptyMessage.orEmpty(),
			textStyle = emptyTextStyle,
			onRefresh = onRefresh
		)

		else -> content()
	}
}

@Composable
private fun RefreshButton(onRefresh: suspend () -> Unit) {
	val coroutineScope = rememberCoroutineScope()

	FilledTonalButton(onClick = {
		coroutineScope.launch { onRefresh() }
	}) {
		Text(text = stringResource(R.string.refresh_button))
	}
}

@Composable
private fun EmptyScreen(
	text: String,
	textStyle: TextStyle,
	onRefresh: suspend () -> Unit
) {
	Column(
		verticalArrangement = Arrangement.spacedBy(10.dp)
	) {
		Text(
			text = text,
			style = textStyle
		)

		RefreshButton(onRefresh)
	}
}

@Composable
private fun ErrorScreen(
	modifier: Modifier = Modifier,
	text: String,
	textStyle: TextStyle,
	onRefresh: suspend () -> Unit = {}
) {
	if (text.isEmpty()) return

	Column(
		modifier = modifier.fillMaxSize(),
		verticalArrangement = Arrangement.spacedBy(
			space = 10.dp,
			alignment = Alignment.CenterVertically
		),
		horizontalAlignment = Alignment.CenterHorizontally
	) {
		Row(
			modifier = Modifier.fillMaxWidth(0.8f)
		) {
			Text(
				text = text,
				style = textStyle,
			)
		}

		RefreshButton(onRefresh)
	}
}

@Composable
private fun LoadingScreen(
	loaderModifier: Modifier = Modifier,
	handleLoading: Boolean
) {
	if (!handleLoading) return

	Box(
		modifier = Modifier.fillMaxSize(),
		contentAlignment = Alignment.Center
	) {
		CircleLoader(modifier = loaderModifier)
	}
}