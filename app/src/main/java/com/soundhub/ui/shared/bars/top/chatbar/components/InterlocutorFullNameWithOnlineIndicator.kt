package com.soundhub.ui.shared.bars.top.chatbar.components

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.soundhub.R
import com.soundhub.data.model.User
import com.soundhub.data.states.ChatUiState
import com.soundhub.ui.pages.chat.ChatViewModel
import com.soundhub.utils.lib.UserUtils

@Composable
internal fun InterlocutorFullNameWithOnlineIndicator(
	chatViewModel: ChatViewModel
) {
	val context: Context = LocalContext.current
	val chatUiState: ChatUiState by chatViewModel.chatUiState.collectAsState()
	val interlocutor: User? = chatUiState.interlocutor

	val friendName: String = "${interlocutor?.firstName} ${interlocutor?.lastName}".trim()

	var isOnline: Boolean by rememberSaveable { mutableStateOf(interlocutor?.isOnline ?: false) }
	var onlineIndicator: Int by rememberSaveable { mutableIntStateOf(R.drawable.offline_indicator) }
	var onlineIndicatorText: String by rememberSaveable { mutableStateOf("") }

	LaunchedEffect(interlocutor) {
		isOnline = interlocutor?.isOnline ?: false
		UserUtils.updateOnlineStatusIndicator(
			context = context,
			user = interlocutor
		) { indicatorIcon, _, text ->
			onlineIndicator = indicatorIcon
			onlineIndicatorText = text
		}
	}

	Column {
		Text(
			text = friendName,
			fontWeight = FontWeight.ExtraBold,
			fontSize = 18.sp,
			lineHeight = 28.sp
		)
		Row(
			verticalAlignment = Alignment.CenterVertically,
			horizontalArrangement = Arrangement.spacedBy(2.dp)
		) {
			Image(
				painter = painterResource(id = onlineIndicator),
				contentDescription = "online indicator",
				modifier = Modifier.size(16.dp)
			)
			Text(
				text = onlineIndicatorText,
				color = MaterialTheme.colorScheme.onSecondaryContainer,
				fontSize = 12.sp
			)
		}
	}
}