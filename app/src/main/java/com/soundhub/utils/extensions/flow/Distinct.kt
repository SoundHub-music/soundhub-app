package com.soundhub.utils.extensions.flow

import androidx.paging.PagingData
import androidx.paging.filter
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlin.collections.mutableSetOf

fun <T : Any> Flow<PagingData<T>>.distinctBy(selector: (T) -> Any?): Flow<PagingData<T>> {
	return this.map {
		val seenIds = mutableSetOf<Any?>()

		it.filter { data ->
			val id = selector(data)
			val contains = seenIds.contains(id)

			if (!contains)
				seenIds.add(id)

			!contains
		}
	}
}