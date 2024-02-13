package com.soundhub.ui.profile.components.sections.user_actions

import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.FilledTonalIconToggleButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.soundhub.R

@Composable
internal fun SendFriendRequestButton(modifier: Modifier = Modifier) {
    FilledTonalIconToggleButton(
        modifier = modifier
            .size(48.dp),
        shape = RoundedCornerShape(10.dp),
        checked = true,
        onCheckedChange = {},
    ) {
        Icon(
            painter = painterResource(id = R.drawable.baseline_person_add),
            contentDescription = null
        )
    }
}