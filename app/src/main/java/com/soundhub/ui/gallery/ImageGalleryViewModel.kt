package com.soundhub.ui.gallery

import androidx.lifecycle.ViewModel
import com.bumptech.glide.load.model.GlideUrl
import com.soundhub.data.datastore.UserCredsStore
import com.soundhub.data.datastore.UserPreferences
import com.soundhub.utils.HttpUtils
import com.soundhub.utils.enums.MediaFolder
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@HiltViewModel
class ImageGalleryViewModel @Inject constructor(
    userCredsStore: UserCredsStore
): ViewModel() {
    private val userCreds: Flow<UserPreferences> = userCredsStore.getCreds()

    fun prepareGlideImage(
        image: String,
        mediaFolder: MediaFolder
    ): GlideUrl? {
        val creds: UserPreferences? = runBlocking { userCreds.firstOrNull() }
        return HttpUtils.prepareGlideUrWithAccessToken(creds, image, mediaFolder)
    }
}