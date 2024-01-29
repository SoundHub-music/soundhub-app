package com.soundhub.ui.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.soundhub.data.model.User
import com.soundhub.ui.authentication.AuthenticationViewModel
import com.soundhub.ui.profile.components.sections.avatar.UserAvatar
import com.soundhub.ui.profile.components.UserProfileContainer
import java.util.UUID

@Composable
fun ProfileScreen(
    userId: UUID? = null,
    authViewModel: AuthenticationViewModel = hiltViewModel()
) {
    val user: User? by remember {
        mutableStateOf(
            User(
                email = "test@test.com",
                firstName = "Anton",
                lastName = "Petrov",
                country = "Russia",
                city = "Moscow",
                description = "This is a description",
                id = userId ?: UUID.randomUUID()
            )
        )
    }

    Column(
        modifier = Modifier
            .fillMaxHeight()
            .background(MaterialTheme.colorScheme.background),
        // negative indent provides an overlay of the content on the avatar
        verticalArrangement = Arrangement.spacedBy((-30).dp)
    ) {
        UserAvatar()

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .clip(RoundedCornerShape(topStart = 30.dp, topEnd = 30.dp))
                .background(
                    MaterialTheme.colorScheme.primaryContainer
                )
        ) {
            Column(
                modifier = Modifier
                    .padding(start = 16.dp, end = 16.dp, top = 30.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                UserProfileContainer(user)
            }
        }
    }
}