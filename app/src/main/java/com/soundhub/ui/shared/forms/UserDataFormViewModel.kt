package com.soundhub.ui.shared.forms

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.soundhub.data.dao.CountryDao
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
    private val countryDao: CountryDao
): ViewModel() {
    val countryList = MutableStateFlow<List<Country>>(emptyList())
    val isLoading = MutableStateFlow(false)

    init { loadCountries() }

    private fun loadCountries() = viewModelScope.launch {
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