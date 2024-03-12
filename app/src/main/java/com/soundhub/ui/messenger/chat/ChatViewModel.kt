package com.soundhub.ui.messenger.chat

import android.util.Log
import androidx.lifecycle.ViewModel
import com.soundhub.data.model.Message
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(): ViewModel() {
    var messages = MutableStateFlow<List<Message>>(emptyList())
        private set

    fun sendMessage(message: Message) {
        messages.value += message
        Log.d("ChatViewModel", "sendMessage[sent message]: $message")
        Log.d("ChatViewModel", "sendMessage[messages state]: ${messages.value}")
    }
}