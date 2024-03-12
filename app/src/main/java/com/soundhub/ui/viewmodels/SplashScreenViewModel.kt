package com.soundhub.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SplashScreenViewModel: ViewModel() {
    var isLoading = MutableStateFlow(true)
        private set

    init {
        viewModelScope.launch {
            delay(2000)
            isLoading.update { false }
        }
    }
}