package com.soundhub.ui.profile.components

import androidx.compose.foundation.background
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
import com.soundhub.data.model.User

@Composable
fun ProfileBody() {
    // state with test data
    val user: User? by remember {
        mutableStateOf(
            User(
                email = "test@test.com",
                firstName = "Anton",
                lastName = "Petrov",
                country = "Russia",
                city = "Moscow"
        ))
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .clip(RoundedCornerShape(topStart = 30.dp, topEnd = 30.dp))
            .background(
                MaterialTheme.colorScheme.primaryContainer
//                    .copy(alpha = 0.5f),
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
