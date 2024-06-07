package com.soundhub.ui.components.avatar

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.soundhub.data.datastore.UserCredsStore
import com.soundhub.data.datastore.UserPreferences
import com.soundhub.utils.HttpUtils
import com.soundhub.utils.enums.MediaFolder
import com.soundhub.utils.enums.UriScheme
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AvatarViewModel @Inject constructor(
    userCredsStore: UserCredsStore
): ViewModel() {
    private val userCreds: Flow<UserPreferences> = userCredsStore.getCreds()
    private val _imageUrl = MutableStateFlow<Any?>(null)
    val imageUrl: StateFlow<Any?> = _imageUrl.asStateFlow()

    fun loadGlideUrlOrImageUri(imageUrl: Uri?) = viewModelScope.launch(Dispatchers.IO) {
        userCreds.collect { creds ->
            val image: Any? = if (imageUrl?.scheme == UriScheme.HTTP.scheme)
                HttpUtils.prepareGlideUrWithAccessToken(creds, imageUrl.toString(), MediaFolder.AVATAR)
            else imageUrl.toString()

            _imageUrl.update { image }
        }

    }
}