package com.soundhub.presentation.pages.chat.ui.containers.message_list

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemContentType
import com.soundhub.domain.model.Chat
import com.soundhub.domain.model.Message
import com.soundhub.domain.model.User
import com.soundhub.domain.states.ChatUiState
import com.soundhub.domain.states.UiState
import com.soundhub.presentation.pages.chat.ChatViewModel
import com.soundhub.presentation.pages.chat.ui.message.MessageBox
import com.soundhub.presentation.shared.buttons.ScrollToBottomButton
import com.soundhub.presentation.shared.pagination.PagingLoadStateContainer
import com.soundhub.presentation.viewmodels.UiStateDispatcher
import kotlinx.coroutines.flow.merge
import kotlinx.coroutines.launch
import java.time.LocalDate

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MessageBoxList(
	modifier: Modifier = Modifier,
	chatViewModel: ChatViewModel,
	uiStateDispatcher: UiStateDispatcher,
	lazyListState: LazyListState
) {
	val coroutineScope = rememberCoroutineScope()

	val layoutInfo by remember { derivedStateOf { lazyListState.layoutInfo } }
	val totalItemCount by remember { derivedStateOf { layoutInfo.totalItemsCount } }

	var unreadMessageCount: Int by remember { mutableIntStateOf(0) }

	val chatUiState: ChatUiState by chatViewModel.chatUiState.collectAsState()
	val currentChat: Chat? = chatUiState.chat

	val uiState by uiStateDispatcher.uiState.collectAsState(initial = UiState())
	val authorizedUser: User? = uiState.authorizedUser

	val pagedMessages = remember(currentChat) {
		chatViewModel.pagingDataState
	}.collectAsLazyPagingItems()

	val messageSnapshot: List<Message> = pagedMessages.itemSnapshotList
		.toList()
		.filterNotNull()

	val receivedMessageChannel = uiStateDispatcher.receivedMessages
	val deletedMessageChannel = uiStateDispatcher.deletedMessages
	val readMessageChannel = uiStateDispatcher.readMessages

	val loadState = pagedMessages.loadState
	val firstVisibleMessageIndex: Int by remember {
		derivedStateOf { lazyListState.firstVisibleItemIndex }
	}

	val stickyDate: LocalDate? by remember {
		derivedStateOf {
			if (pagedMessages.itemCount == 0)
				return@derivedStateOf null

			runCatching {
				val message: Message? = pagedMessages.peek(lazyListState.firstVisibleItemIndex)
				return@runCatching message?.createdAt?.toLocalDate()
			}.getOrNull()
		}
	}


	LaunchedEffect(key1 = messageSnapshot) {
		chatViewModel.cacheMessages(messageSnapshot)

		unreadMessageCount = messageSnapshot.count {
			it.isNotReadAndNotSentBy(authorizedUser)
		}
	}

	// updating page when message from one of 3 channels is coming
	LaunchedEffect(
		key1 = receivedMessageChannel,
		key2 = readMessageChannel,
		key3 = deletedMessageChannel
	) {
		merge(receivedMessageChannel, readMessageChannel, deletedMessageChannel)
			.collect { pagedMessages.refresh() }
	}

	// updating read messages
	LaunchedEffect(
		key1 = firstVisibleMessageIndex,
		key2 = totalItemCount,
	) {
		val messages = pagedMessages.itemSnapshotList
			.toList()
			.filterNotNull()

		chatViewModel.readVisibleMessagesFromIndex(
			firstVisibleMessageIndex,
			messages
		)
	}

	Box(modifier = modifier.fillMaxSize()) {
		PagingLoadStateContainer(loadState)
		LazyColumn(
			state = lazyListState,
			reverseLayout = true,
			modifier = Modifier.fillMaxSize(),
			verticalArrangement = Arrangement.spacedBy(10.dp),
		) {
			stickyHeader(contentType = pagedMessages.itemContentType()) {
				stickyDate?.let { MessageDateChip(it) }
			}

			items(
				count = pagedMessages.itemCount,
				key = { pagedMessages.peek(it)?.id ?: it },
				contentType = { "message" }
			) { index ->
				pagedMessages[index]?.let { message ->
					Box(modifier = Modifier.animateItem()) {
						MessageBox(
							modifier = Modifier,
							message = message,
							isOwnMessage = message.author?.id == authorizedUser?.id,
							chatViewModel = chatViewModel,
							lazyListState = lazyListState
						)
					}
				}
			}
		}

		ScrollToBottomButton(
			lazyListState = lazyListState,
			unreadMessageCount = unreadMessageCount,
			onClick = {
				coroutineScope.launch {
					chatViewModel.scrollToLastMessage(
						lazyListState = lazyListState,
						reverse = true,
						animate = true
					)
				}
			})
	}
}