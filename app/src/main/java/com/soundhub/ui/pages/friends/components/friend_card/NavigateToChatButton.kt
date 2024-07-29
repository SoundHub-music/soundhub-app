package com.soundhub.ui.pages.friends.components.friend_card

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.soundhub.R
import com.soundhub.data.model.User
import com.soundhub.ui.pages.friends.FriendsViewModel

@Composable
internal fun NavigateToChatButton(
    user: User,
    friendsViewModel: FriendsViewModel
) {
    FilledTonalIconButton(
        onClick = { friendsViewModel.onNavigateToChatBtnClick(user) },
        shape = RoundedCornerShape(5.dp),
        modifier = Modifier,
    ) {
        Icon(
            painter = painterResource(id = R.drawable.baseline_forward_to_inbox_24),
            contentDescription = "send message"
        )
    }
}
