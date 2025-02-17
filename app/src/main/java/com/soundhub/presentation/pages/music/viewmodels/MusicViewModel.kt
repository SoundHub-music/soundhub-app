package com.soundhub.presentation.pages.music.viewmodels

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.soundhub.R
import com.soundhub.Route
import com.soundhub.data.local_database.dao.UserDao
import com.soundhub.domain.model.Artist
import com.soundhub.domain.model.Genre
import com.soundhub.domain.model.LibraryItemData
import com.soundhub.domain.model.User
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MusicViewModel @Inject constructor(
	private val userDao: UserDao
) : ViewModel() {
	private val _favoriteGenres = MutableStateFlow<List<Genre>>(emptyList())
	val favoriteGenres: StateFlow<List<Genre>> = _favoriteGenres.asStateFlow()

	private val _favoriteArtists = MutableStateFlow<List<Artist>>(emptyList())
	val favoriteArtists: StateFlow<List<Artist>> = _favoriteArtists.asStateFlow()

//	private val _pagerLockState = MutableStateFlow(false)
//	val pagerLockState = _pagerLockState.asStateFlow()

	@Composable
	fun getMenuItems(): List<LibraryItemData> = listOf(
		LibraryItemData(
			title = stringResource(id = R.string.music_library_page_playlists),
			icon = painterResource(id = R.drawable.round_queue_music_24),
			route = Route.Music.Playlists.route
		),
		LibraryItemData(
			title = stringResource(id = R.string.music_library_page_favorites),
			icon = painterResource(id = R.drawable.rounded_genres_24),
			route = Route.Music.FavoriteGenres.route
		),
		LibraryItemData(
			title = stringResource(id = R.string.music_library_page_artists),
			icon = painterResource(id = R.drawable.rounded_artist_24),
			route = Route.Music.FavoriteArtists.route
		)
	)

//	fun setPagerLock(value: Boolean) = _pagerLockState.update { value }

//	fun horizontalDragHandler(pointerInputScope: PointerInputScope) = viewModelScope.launch {
//		pointerInputScope.detectHorizontalDragGestures(
//			onDragStart = { setPagerLock(true) },
//			onDragEnd = { setPagerLock(false) },
//			onDragCancel = { setPagerLock(false) }
//		) { change, _ -> change.consume() }
//	}

	fun loadUserFavoriteGenres() = viewModelScope.launch(Dispatchers.IO) {
		val authorizedUser: User? = userDao.getCurrentUser()
		_favoriteGenres.update { authorizedUser?.favoriteGenres.orEmpty() }
	}

	fun loadUserFavoriteArtists() = viewModelScope.launch(Dispatchers.IO) {
		val authorizedUser: User? = userDao.getCurrentUser()
		_favoriteArtists.update { authorizedUser?.favoriteArtists.orEmpty() }
	}
}