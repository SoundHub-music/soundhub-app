package com.soundhub.ui.components.forms

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.soundhub.data.dao.CountryDao
import com.soundhub.data.database.AppDatabase
import com.soundhub.data.model.Country
import com.soundhub.data.repository.CountryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserDataFormViewModel @Inject constructor(
    private val countryRepository: CountryRepository,
    appDb: AppDatabase
): ViewModel() {
    val countryList = MutableStateFlow<List<Country>>(emptyList())
    val isLoading = MutableStateFlow(false)
    private val countryDao: CountryDao = appDb.countryDao()

    init {
        viewModelScope.launch {
            loadCountries()
        }
    }

    private suspend fun loadCountries() {
        isLoading.value = true
        var countries: List<Country> = countryDao.getCountries()

        if (countries.isEmpty()) {
            countryRepository.getAllCountryNames()
                .onSuccess { response -> countries = response.body.orEmpty() }
        }
        countryList.update { countries.sortedBy { it.translations.rus.common } }
        isLoading.value = false
    }
}