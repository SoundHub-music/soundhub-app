package com.soundhub.ui.components.bars.top

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.KeyboardArrowLeft
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.soundhub.R
import com.soundhub.Route
import com.soundhub.data.model.User
import com.soundhub.ui.components.avatar.CircularAvatar
import com.soundhub.ui.messenger.chat.ChatUiState
import com.soundhub.ui.messenger.chat.ChatViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatTopAppBar(
    navController: NavHostController,
    chatViewModel: ChatViewModel
) {
    val chatUiState by chatViewModel.chatUiState.collectAsState()

    TopAppBar(
        title = { InterlocutorDetails(
            chatUiState = chatUiState,
            navController = navController
        ) },
        navigationIcon = {
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(
                    imageVector = Icons.AutoMirrored.Rounded.KeyboardArrowLeft,
                    contentDescription = stringResource(id = R.string.btn_description_back),
                    modifier = Modifier.size(32.dp)
                )
            }
        },
        actions = { ChatTopBarActions(
            navController = navController,
            chatState = chatUiState,
            chatViewModel = chatViewModel
        ) }
    )
}

@Composable
private fun InterlocutorDetails(
    chatUiState: ChatUiState,
    navController: NavHostController
) {
    val friendName = "${chatUiState.interlocutor?.firstName} ${chatUiState.interlocutor?.lastName}"
        .trim()

    val interlocutor: User? = chatUiState.interlocutor
    val isOnline = false
    val indicator = painterResource(
        id = if (isOnline) R.drawable.online_indicator
        else R.drawable.offline_indicator
    )

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        modifier = Modifier
            .clip(RoundedCornerShape(5.dp))
            .clickable { onInterlocutorDetailsClick(interlocutor, navController) }
            .padding(horizontal = 10.dp)
    ) {
        CircularAvatar(modifier = Modifier.size(40.dp))

        // user name and online status
        Column {
            Text(
                text = friendName,
                fontWeight = FontWeight.ExtraBold,
                fontSize = 18.sp,
                lineHeight = 28.sp
            )
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(2.dp),
                modifier = Modifier
            ) {
                Image(
                    painter = indicator,
                    contentDescription = "online indicator",
                    modifier = Modifier.size(16.dp)
                )
                Text(
                    text = "Был в сети",
                    color = MaterialTheme.colorScheme.onSecondaryContainer,
                    fontSize = 12.sp
                )
            }
        }
    }
}

private fun onInterlocutorDetailsClick(
    interlocutor: User?,
    navController: NavHostController
) = interlocutor?.let {
        navController.navigate(Route.Profile
                .getStringRouteWithNavArg(it.id.toString()))
}