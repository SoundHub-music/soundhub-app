package com.soundhub.data.sources

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.soundhub.data.api.responses.internal.HttpResult
import com.soundhub.data.api.responses.internal.PageableMessagesResponse
import com.soundhub.domain.model.Message
import com.soundhub.domain.repository.MessageRepository
import java.util.UUID

class MessageSource(
	private val messageRepository: MessageRepository,
	private val chatId: UUID?,
	private val pageSize: Int
) : PagingSource<Int, Message>() {
	override fun getRefreshKey(state: PagingState<Int, Message>): Int? {
		return state.anchorPosition?.let { anchorPosition ->
			val anchorPage = state.closestPageToPosition(anchorPosition)
			anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
		}
	}

	override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Message> {
		try {
			val nextPageNumber = params.key ?: 1
			val response: HttpResult<PageableMessagesResponse>? = chatId?.let {
				messageRepository.getPagedMessages(
					chatId = it,
					page = nextPageNumber,
					pageSize = pageSize
				)
			}

			val data: List<Message> = response?.getOrNull()?.content.orEmpty()

			return LoadResult.Page(
				prevKey = if (nextPageNumber > 1) nextPageNumber - 1 else null,
				nextKey = if (data.isNotEmpty()) nextPageNumber + 1 else null,
				data = data
			)
		} catch (e: Error) {
			return LoadResult.Error(e)
		}
	}

}