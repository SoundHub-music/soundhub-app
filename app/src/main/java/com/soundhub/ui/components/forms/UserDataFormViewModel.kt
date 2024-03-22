package com.soundhub.ui.components.forms

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.soundhub.UiEvent
import com.soundhub.ui.viewmodels.UiStateDispatcher
import com.soundhub.data.api.responses.HttpResult
import com.soundhub.data.model.Country
import com.soundhub.data.repository.CountryRepository
import com.soundhub.domain.usecases.GetImageUseCase
import com.soundhub.utils.MediaFolder
import com.soundhub.utils.UiText
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

@HiltViewModel
class UserDataFormViewModel @Inject constructor(
    private val countryRepository: CountryRepository,
    private val uiStateDispatcher: UiStateDispatcher,
    private val getImageUseCase: GetImageUseCase
): ViewModel() {
    var countryList = MutableStateFlow<List<Country>>(emptyList())
        private set

    var isLoading = MutableStateFlow(false)
        private set

    init {
        viewModelScope.launch {
            isLoading.value = true
            val response: HttpResult<List<Country>> = countryRepository.getAllCountryNames()
            response.onSuccess { countries ->
                countryList.value = countries.body
                    ?.sortedBy { it.translations.rus.common } ?: emptyList()
            }
            .onFailure {
                uiStateDispatcher.sendUiEvent(
                    UiEvent.ShowToast(
                        UiText.DynamicString(
                            it.errorBody?.detail ?: it.throwable?.message ?: ""
                        )
                    )
                )
            }
            isLoading.value = false
        }
    }

    suspend fun getAvatar(avatarUrl: String?, accessToken: String?): File? {
        return getImageUseCase(
            fileName = avatarUrl,
            accessToken = accessToken,
            folderName = MediaFolder.Avatar.name
        )
    }
}