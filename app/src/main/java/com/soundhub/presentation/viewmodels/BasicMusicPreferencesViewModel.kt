package com.soundhub.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.soundhub.domain.model.Artist
import com.soundhub.domain.model.Genre
import com.soundhub.domain.repository.MusicRepository
import com.soundhub.domain.states.ArtistUiState
import com.soundhub.domain.states.GenreUiState
import com.soundhub.domain.usecases.music.LoadGenresUseCase
import com.soundhub.utils.constants.Constants
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@OptIn(FlowPreview::class)
open class BaseMusicPreferencesViewModel(
	private val loadGenresUseCase: LoadGenresUseCase,
	private val musicRepository: MusicRepository,
	private val uiStateDispatcher: UiStateDispatcher
) : ViewModel() {
	protected val _genreUiState: MutableStateFlow<GenreUiState> = MutableStateFlow(GenreUiState())
	val genreUiState = _genreUiState.asStateFlow()

	protected val _artistUiState: MutableStateFlow<ArtistUiState> =
		MutableStateFlow(ArtistUiState())

	val artistUiState = _artistUiState.asStateFlow()

	private var searchText = MutableStateFlow<String?>(null)

	init {
		uiStateDispatcher.onSearchValueDebounceChange { text ->
			searchText.update { text }
		}
	}

	open fun loadGenres() = viewModelScope.launch(Dispatchers.IO) {
		loadGenresUseCase(genreUiState = _genreUiState)
	}

	@OptIn(ExperimentalCoroutinesApi::class)
	fun getArtistPage(): Flow<PagingData<Artist>> {
		return searchText.flatMapLatest { text ->
			musicRepository.getArtistPage(
				genreUiState = _genreUiState.value,
				searchText = text,
				pageSize = Constants.DEFAULT_ARTIST_PAGE_SIZE,
			).cachedIn(viewModelScope)
		}
	}

	open fun onNextButtonClick(): Job {
		throw NotImplementedError(Constants.METHOD_NOT_IMPLEMENTED_ERROR)
	}

	protected open fun onChooseGenresNextButtonClick(): Job {
		throw NotImplementedError(Constants.METHOD_NOT_IMPLEMENTED_ERROR)
	}

	protected open fun onChooseArtistsNextButtonClick(): Job {
		throw NotImplementedError(Constants.METHOD_NOT_IMPLEMENTED_ERROR)
	}

	fun onArtistItemClick(isChosen: Boolean, artist: Artist) = viewModelScope.launch {
		if (isChosen) addChosenArtist(artist)
		else {
			val filteredChosenArtists = _artistUiState.value
				.chosenArtists
				.filter { it.id != artist.id }

			setChosenArtists(filteredChosenArtists)
		}
	}

	fun onGenreItemClick(isChosen: Boolean, genre: Genre) = viewModelScope.launch {
		if (isChosen) {
			addChosenGenre(genre)
			return@launch
		}

		val filteredChosenGenres = _genreUiState.value.chosenGenres.filter { it.id != genre.id }
		setChosenGenres(filteredChosenGenres)
	}

	fun onSearchFieldChange(value: String) = viewModelScope.launch {
		uiStateDispatcher.updateSearchBarText(value)
	}

	private fun setChosenGenres(genres: List<Genre>) = _genreUiState.update {
		it.copy(chosenGenres = genres)
	}

	private fun addChosenGenre(genre: Genre) = _genreUiState.update {
		it.copy(chosenGenres = it.chosenGenres + genre)
	}

	private fun setChosenArtists(artists: List<Artist>) = _artistUiState.update {
		it.copy(chosenArtists = artists)
	}

	private fun addChosenArtist(artist: Artist) = _artistUiState.update {
		it.copy(chosenArtists = it.chosenArtists + artist)
	}
}
