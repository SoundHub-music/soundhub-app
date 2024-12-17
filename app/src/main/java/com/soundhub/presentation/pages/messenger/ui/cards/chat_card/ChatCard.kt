package com.soundhub.presentation.pages.messenger.ui.cards.chat_card

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.unit.dp
import com.soundhub.domain.model.Chat
import com.soundhub.presentation.pages.messenger.MessengerViewModel
import com.soundhub.presentation.pages.messenger.ui.cards.chat_card.components.ChatCardContainer
import com.soundhub.presentation.viewmodels.UiStateDispatcher

@Composable
internal fun ChatCard(
	chat: Chat,
	uiStateDispatcher: UiStateDispatcher,
	messengerViewModel: MessengerViewModel
) {
	Card(
		colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
		border = BorderStroke(1.dp, MaterialTheme.colorScheme.inverseOnSurface),
		shape = RoundedCornerShape(12.dp),
		onClick = { messengerViewModel.onChatCardClick(chat) },
		modifier = Modifier
			.fillMaxWidth()
			.padding(5.dp)
			.shadow(5.dp, RoundedCornerShape(12.dp))
	) {
		ChatCardContainer(
			messengerViewModel = messengerViewModel,
			uiStateDispatcher = uiStateDispatcher,
			chat = chat
		)
	}
}