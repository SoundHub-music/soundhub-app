package com.soundhub.ui.messenger.chat

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.soundhub.data.model.Message
import com.soundhub.ui.messenger.chat.events.ChatEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(): ViewModel() {
    var messages = MutableStateFlow<List<Message>>(emptyList())
        private set

    var chatEvent = Channel<ChatEvent>()


    fun sendMessage(message: Message) = messages.update {
        it + message
    }


    private fun onEvent(event: ChatEvent) {
        viewModelScope.launch {
            when (event) {
                is ChatEvent.SendMessage -> {

                }
            }
        }
    }
    fun sendEvent(event: ChatEvent) {
        viewModelScope.launch {
            chatEvent.send(event)
        }
    }
}