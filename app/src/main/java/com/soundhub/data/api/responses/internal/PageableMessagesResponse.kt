package com.soundhub.data.api.responses.internal

import com.soundhub.domain.model.Message

data class PageableMessagesResponse(
	val content: List<Message> = emptyList(),
	val pageable: PageableMessagesInfo = PageableMessagesInfo(),
	val last: Boolean = true,
	val first: Boolean = true,
	val empty: Boolean = true,

	val numberOfElements: Int = 0,
	val totalPages: Int = 0,
	val totalElements: Int = 0,
	val size: Int = 0,
	val number: Int = 0,
	val sort: PageableMessagesSortInfo = PageableMessagesSortInfo()
)

data class PageableMessagesInfo(
	val pageNumber: Int = 0,
	val pageSize: Int = 0,
	val offset: Int = 0,
	val paged: Boolean = true,
	val unpaged: Boolean = false,
	val sort: PageableMessagesSortInfo = PageableMessagesSortInfo()
) {
	val offsetPageNumber: Int
		get() = pageNumber + 1
}

data class PageableMessagesSortInfo(
	val sorted: Boolean = false,
	val unsorted: Boolean = true,
	val empty: Boolean = true
)

enum class PagedMessageOrderType(val type: String) {
	ASC("asc"),
	DESC("desc")
}