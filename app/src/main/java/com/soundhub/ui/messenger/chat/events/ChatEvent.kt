package com.soundhub.ui.messenger.chat.events

sealed class ChatEvent {
    data class SendMessage(val message: String): ChatEvent()
}