package com.soundhub.ui.shared.avatar

import android.net.Uri
import androidx.lifecycle.ViewModel
import com.soundhub.data.datastore.UserCredsStore
import com.soundhub.data.datastore.model.UserPreferences
import com.soundhub.utils.lib.HttpUtils
import com.soundhub.utils.enums.MediaFolder
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import javax.inject.Inject

@HiltViewModel
class AvatarViewModel @Inject constructor(
    userCredsStore: UserCredsStore
): ViewModel() {
    private val userCreds: Flow<UserPreferences> = userCredsStore.getCreds()

    suspend fun getGlideUrlOrImageUri(imageUri: Uri?): Any? {
        val creds = userCreds.firstOrNull()
        return HttpUtils.getGlideUrlOrImagePath(creds, imageUri, MediaFolder.AVATAR)
    }
}