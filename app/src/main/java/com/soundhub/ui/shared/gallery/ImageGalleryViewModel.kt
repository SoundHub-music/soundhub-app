package com.soundhub.ui.shared.gallery

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bumptech.glide.load.model.GlideUrl
import com.soundhub.data.datastore.UserCredsStore
import com.soundhub.data.datastore.model.UserPreferences
import com.soundhub.utils.enums.MediaFolder
import com.soundhub.utils.lib.HttpUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ImageGalleryViewModel @Inject constructor(
	userCredsStore: UserCredsStore
) : ViewModel() {
	private val userCredsFlow: Flow<UserPreferences> = userCredsStore.getCreds()
	private val userCredsInstance = MutableStateFlow<UserPreferences?>(null)

	init {
		viewModelScope.launch(Dispatchers.Main) {
			userCredsInstance.update { userCredsFlow.firstOrNull() }
		}
	}

	fun getGlideUrlOrImageUri(
		image: String,
		mediaFolder: MediaFolder
	): GlideUrl? = HttpUtils
		.prepareGlideUrWithAccessToken(
			userCredsInstance.value,
			image,
			mediaFolder
		)
}