package com.soundhub.ui.messenger.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import java.util.UUID

data class ConversationItem(
    val firstName: String,
    val lastName: String,
    val lastMessage: String = "",
    val userAvatarUrl: String? = null,
    val id: UUID = UUID.randomUUID()
)

@Composable
fun ConversationList(
    modifier: Modifier = Modifier,
    conversations: List<ConversationItem> = emptyList()
) {
    LazyColumn(
        modifier = modifier.fillMaxWidth()
            .padding(top = 5.dp, bottom = 5.dp),
        verticalArrangement = Arrangement.spacedBy(5.dp)
    ) {
        items(items = conversations, key = { it.id }) {
            ConversationCard(data = it)
        }
    }
}



@Composable
@Preview
fun ConversationListPreview() {
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

    ConversationList(conversations = conversations)
}