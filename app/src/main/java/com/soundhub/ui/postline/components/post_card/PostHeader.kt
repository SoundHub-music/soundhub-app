package com.soundhub.ui.postline.components.post_card

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.MoreVert
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.soundhub.Route
import com.soundhub.data.model.User
import com.soundhub.ui.components.CircularAvatar
import com.soundhub.utils.DateFormatter
import java.time.LocalDateTime

@Composable
internal fun PostHeader(
    modifier: Modifier = Modifier,
    avatarUrl: String?,
    postAuthor: User?,
    publishDate: LocalDateTime,
    navController: NavHostController
) {
    Row(
        modifier = modifier
            .padding(horizontal = 16.dp, vertical = 12.dp)
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            CircularAvatar(
                imageUrl = avatarUrl,
                contentDescription = "${postAuthor?.firstName} ${postAuthor?.lastName}".trim(),
                modifier = Modifier
                    .size(40.dp)
                    .clickable {
                        navController.navigate(Route.Profile(postAuthor?.id.toString()).route)
                    }
            )

            Column {
                Text(
                    text = "${postAuthor?.firstName} ${postAuthor?.lastName}".trim(),
                    fontSize = 16.sp,
                    lineHeight = 24.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 0.15.sp
                )
                Text(
                    text = DateFormatter.getRelativeDate(publishDate),
                    fontSize = 16.sp,
                    lineHeight = 20.sp,
                    fontWeight = FontWeight.Medium,
                    letterSpacing = 0.25.sp
                )
            }
        }

        IconButton(onClick = { /*TODO*/ }) {
            Icon(imageVector = Icons.Rounded.MoreVert, contentDescription = "post parameters")
        }
    }
}