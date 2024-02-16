package com.soundhub.ui.profile

import androidx.lifecycle.ViewModel
import com.soundhub.data.model.User
import dagger.hilt.android.lifecycle.HiltViewModel
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(): ViewModel() {

    fun getUserById(userId: UUID): User? {return null}
}