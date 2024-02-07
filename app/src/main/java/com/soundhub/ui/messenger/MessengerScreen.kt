package com.soundhub.ui.messenger

import androidx.compose.runtime.Composable
import com.soundhub.ui.components.ContentContainer
import com.soundhub.ui.messenger.components.ConversationItem
import com.soundhub.ui.messenger.components.ConversationList

@Composable
fun MessengerScreen() {
    val conversations = listOf(
        ConversationItem(
            "Name",
            "LastName",
            "last message"
        ),

        ConversationItem(
            "Name",
            "LastName",
            "last message"
        ),

        ConversationItem(
            "Name",
            "LastName",
            "last message"
        )
    )

    ContentContainer {
        ConversationList(conversations = conversations)
    }
}