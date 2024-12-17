package com.soundhub.presentation.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.soundhub.data.enums.ApiStatus
import com.soundhub.domain.model.Artist
import com.soundhub.domain.model.Genre
import com.soundhub.domain.states.ArtistUiState
import com.soundhub.domain.states.GenreUiState
import com.soundhub.domain.usecases.music.LoadArtistsUseCase
import com.soundhub.domain.usecases.music.LoadGenresUseCase
import com.soundhub.domain.usecases.music.SearchArtistsUseCase
import com.soundhub.utils.constants.Constants
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@OptIn(FlowPreview::class)
open class BaseMusicPreferencesViewModel(
	private val loadGenresUseCase: LoadGenresUseCase,
	private val loadArtistsUseCase: LoadArtistsUseCase,
	private val searchArtistsUseCase: SearchArtistsUseCase,
	private val uiStateDispatcher: UiStateDispatcher
) : ViewModel() {
	protected val _genreUiState: MutableStateFlow<GenreUiState> = MutableStateFlow(GenreUiState())
	val genreUiState = _genreUiState.asStateFlow()

	protected val _artistUiState: MutableStateFlow<ArtistUiState> =
		MutableStateFlow(ArtistUiState())
	val artistUiState = _artistUiState.asStateFlow()

	protected var searchJob: Job? = null
	protected var loadArtistsJob: Job? = null

	init {
		viewModelScope.launch {
			val debouncedSearchBarText: Flow<String> =
				uiStateDispatcher.uiState
					.map { it.searchBarText }
					.debounce(500)
					.distinctUntilChanged()

			debouncedSearchBarText.collect { query ->
				if (query.isEmpty()) {
					loadArtists()
				} else {
					searchArtists(query)
				}
			}
		}
	}

	open fun loadGenres() = viewModelScope.launch(Dispatchers.IO) {
		loadGenresUseCase(genreUiState = _genreUiState)
	}

	open fun loadArtists(paginationUrl: String? = null) {
		loadArtistsJob?.cancel()
		loadArtistsJob = viewModelScope.launch(Dispatchers.IO) {
			_artistUiState.update { it.copy(status = ApiStatus.LOADING) }.also {
				loadArtistsUseCase(
					artistUiState = _artistUiState,
					genreUiState = _genreUiState.value,
					paginationUrl = paginationUrl
				)
					.onSuccess { _artistUiState.update { it.copy(status = ApiStatus.SUCCESS) } }
					.onFailure { _artistUiState.update { it.copy(status = ApiStatus.ERROR) } }
			}
		}
	}

	private fun searchArtists(value: String) {
		try {
			searchJob?.cancel()
			searchJob = viewModelScope.launch(Dispatchers.IO) {
				searchArtistsUseCase(searchString = value)
					.onSuccess { response ->
						_artistUiState.update {
							it.copy(
								status = ApiStatus.SUCCESS,
								artists = response
							)
						}
					}
					.onFailure { _artistUiState.update { it.copy(status = ApiStatus.ERROR) } }
			}
		} catch (exception: Exception) {
			Log.e("BasicMusicPreferencesViewModel", "searchArtists: ${exception.localizedMessage}")
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
