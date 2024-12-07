package com.soundhub.ui.shared.pagination

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.paging.CombinedLoadStates
import androidx.paging.LoadState
import com.soundhub.ui.shared.containers.CircleLoaderBlock


@Composable
fun PagingLoadStateContainer(
	loadState: CombinedLoadStates,
	onRefreshLoading: @Composable () -> Unit = {},
	onRefreshNotLoading: @Composable () -> Unit = {},
	onRefreshError: @Composable () -> Unit = {},

	onPrependLoading: @Composable () -> Unit = {},
	onPrependNotLoading: @Composable () -> Unit = {},
	onPrependError: @Composable () -> Unit = {},

	) {
	var isLoading by remember { mutableStateOf(true) }

	val setLoading = { isLoading = true }
	val unsetLoading = { isLoading = false }

	when (loadState.refresh) {
		is LoadState.Loading -> {
			if (!loadState.refresh.endOfPaginationReached) {
				setLoading()
				onRefreshLoading()
			}
		}

		is LoadState.Error -> {
			unsetLoading()
			onRefreshError()
		}

		is LoadState.NotLoading -> {
			unsetLoading()
			onRefreshNotLoading()
		}
	}

	when (loadState.prepend) {
		is LoadState.Loading -> {
			setLoading()
			onPrependLoading()
		}

		is LoadState.Error -> {
			unsetLoading()
			onPrependError()
		}

		is LoadState.NotLoading -> {
			unsetLoading()
			onPrependNotLoading()
		}
	}

	CircleLoaderBlock(isLoading)
}