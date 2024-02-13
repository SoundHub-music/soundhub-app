package com.soundhub.ui.components.forms

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.soundhub.data.model.Country
import com.soundhub.data.repository.CountryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class UserDataFormViewModel @Inject constructor(
    private val countryRepository: CountryRepository
): ViewModel() {
    var countryList = MutableStateFlow<List<Country>>(emptyList())
        private set

    var isLoading = MutableStateFlow(false)
        private set

    init {
        viewModelScope.launch {
            isLoading.value = true
            val response: Response<List<Country>> = countryRepository.getAllCountryNames()
            if (response.isSuccessful) {
                countryList.value = response.body()?.sortedBy { it.translations.rus.common } ?: emptyList()
                isLoading.value = false
            }

        }
    }
}