package com.soundhub.presentation.pages.gallery

import android.util.Log
import androidx.compose.foundation.pager.PagerState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import com.soundhub.Route
import com.soundhub.data.datastore.UserCredsStore
import com.soundhub.data.datastore.model.UserPreferences
import com.soundhub.presentation.viewmodels.UiStateDispatcher
import com.soundhub.utils.constants.Constants
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HorizontalImagePagerViewModel @Inject constructor(
	userCredsStore: UserCredsStore,
	private val uiStateDispatcher: UiStateDispatcher
) : ViewModel() {
	private val userCredsFlow: Flow<UserPreferences> = userCredsStore.getCreds()
	private val userCredsInstance = MutableStateFlow<UserPreferences?>(null)

	val userCreds: StateFlow<UserPreferences?> = userCredsInstance.asStateFlow()

	init {
		viewModelScope.launch(Dispatchers.IO) {
			userCredsInstance.update { userCredsFlow.firstOrNull() }
		}
	}

	override fun onCleared() {
		super.onCleared()
		Log.d("HorizontalImagePagerViewModel", "viewmodel was cleared")

		uiStateDispatcher.setGalleryUrls(emptyList())
	}

	fun onImageClick(
		clickable: Boolean = false,
		navController: NavHostController?,
		images: List<String>,
		subFolder: String?,
		page: Int
	) {
		if (!clickable) return

		uiStateDispatcher.setGalleryUrls(images)

		var route = "${Route.Gallery.route}/${page}"

		if (subFolder != null) {
			route += "?${Constants.GALLERY_SUBFOLDER_NAV_ARG}=$subFolder"
		}

		navController?.navigate(route)
	}

	fun getImageOpacity(pagerState: PagerState, page: Int): Float {
		return if (pagerState.currentPage == page) 1f else 0.5f
	}
}