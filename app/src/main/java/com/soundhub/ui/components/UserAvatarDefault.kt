package com.soundhub.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.soundhub.R

@Composable
fun UserAvatarDefault(firstName: String? = "", lastName: String? = "", size: Dp) {
    Box(
        modifier = Modifier
            .background(
                color = MaterialTheme.colorScheme.secondaryContainer,
                shape = CircleShape
            )
            .size(size),
        contentAlignment = Alignment.Center
    ) {
        if (firstName?.isNotEmpty() == true && lastName?.isNotEmpty() == true)
            Text(
                text = "${firstName[0]}${lastName[0]}",
                fontSize = 14.sp
            )
        else Image(
            painter = painterResource(id = R.drawable.user),
            contentDescription = null,
            modifier = Modifier.size(40.dp)
        )
    }
}