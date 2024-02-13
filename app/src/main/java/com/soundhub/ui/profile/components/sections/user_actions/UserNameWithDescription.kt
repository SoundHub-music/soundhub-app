package com.soundhub.ui.profile.components.sections.user_actions

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.KeyboardArrowDown
import androidx.compose.material.icons.rounded.KeyboardArrowUp
import androidx.compose.material3.Icon
import androidx.compose.material3.IconToggleButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.soundhub.data.datastore.UserPreferences

@Composable
internal fun UserNameWithDescription(user: UserPreferences? = null) {
    var isDescriptionButtonChecked: Boolean by rememberSaveable {
        mutableStateOf(false)
    }

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "${user?.firstName} ${user?.lastName}".trim(),
            fontWeight = FontWeight.Bold,
            lineHeight = 16.sp,
            fontSize = 28.sp,
            modifier = Modifier
        )

        IconToggleButton(
            checked = isDescriptionButtonChecked,
            onCheckedChange = { isDescriptionButtonChecked = it },
        ) {
            Icon(
                imageVector =
                if (isDescriptionButtonChecked) Icons.Rounded.KeyboardArrowUp
                else Icons.Rounded.KeyboardArrowDown,
                contentDescription = "expand_description",
                modifier = Modifier.size(35.dp)
            )
        }
    }
    if (isDescriptionButtonChecked)
        UserDescriptionBlock(user)
}