package com.soundhub.ui.messenger.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun ConversationCard(data: ConversationItem?) {
    val lastMessage = "Hi there!"

    Card(
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.inverseOnSurface),
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(5.dp)
            .shadow(
                elevation = 5.dp,
                shape = RoundedCornerShape(12.dp),
                ambientColor = Color.Cyan,
            )
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            if (data?.userAvatarUrl == null)
                UserAvatarDefault(firstName = data?.firstName, lastName = data?.lastName)
            else
                GlideImage(
                    model = data.userAvatarUrl,
                    contentDescription = "${data.firstName}'s avatar",
                    modifier = Modifier.size(40.dp)
                )
            Column() {
                Text(
                    text = "${data?.firstName} ${data?.lastName}".trim(),
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 16.sp,
                    lineHeight = 24.sp
                )
                Text(
                    text = lastMessage,
                    fontWeight = FontWeight.Light,
                    fontSize = 14.sp,
                    lineHeight = 20.sp
                )
            }
        }
    }
}

@Composable
@Preview(name = "ConversationCard", showBackground = true)
fun ConversationCardPreview() {
    ConversationCard(data = null)
}


@Composable
fun UserAvatarDefault(firstName: String? = "", lastName: String? = "") {
    Box(
        modifier = Modifier
            .background(
                color = MaterialTheme.colorScheme.secondaryContainer,
                shape = CircleShape
            )
            .size(40.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "${firstName?.get(0)}${lastName?.get(0)}",
            fontSize = 14.sp
        )
    }
}