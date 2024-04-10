package com.soundhub.ui.messenger.chat

import android.util.Log
import androidx.lifecycle.ViewModel
import com.soundhub.data.model.Message
import com.soundhub.data.model.User
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(): ViewModel() {
    var chatState: MutableStateFlow<ChatState> = MutableStateFlow(ChatState())
        private set

    fun setInterlocutor(user: User?) = chatState.update {
        it.copy(interlocutor = user)
    }

    fun sendMessage(message: Message) {
        chatState.update {
            it.copy(messages = it.messages + message)
        }
        Log.d("ChatViewModel", "sendMessage[sent message]: $message")
        Log.d("ChatViewModel", "sendMessage[messages state]: ${chatState.value}")
    }
}