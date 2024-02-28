package com.soundhub.ui.profile.components.sections.user_actions

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import com.soundhub.data.datastore.UserPreferences

@Composable
internal fun UserDescriptionBlock(modifier: Modifier = Modifier, user: UserPreferences? = null) {
    // TODO: expand the list of user data
    Column(modifier = modifier) {
        Text(
            text = "О себе: ${user?.description ?: ""}",
            fontWeight = FontWeight.Medium
        )
        Text(
            text = "День рождения: ",
            fontWeight = FontWeight.Medium
        )
    }
}