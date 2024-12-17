package com.soundhub.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SplashScreenViewModel : ViewModel() {
	val isLoading = MutableStateFlow(true)

	override fun onCleared() {
		super.onCleared()
		isLoading.update { true }
	}

	init {
		viewModelScope.launch {
			delay(1000)
			isLoading.update { false }
		}
	}
}