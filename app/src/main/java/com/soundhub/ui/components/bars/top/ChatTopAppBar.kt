package com.soundhub.ui.components.bars.top

import android.content.Context
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
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
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
import com.soundhub.ui.states.UiState
import com.soundhub.ui.viewmodels.UiStateDispatcher
import com.soundhub.utils.DateFormatter
import java.time.LocalDateTime
import java.time.Month

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatTopAppBar(
    navController: NavHostController,
    chatViewModel: ChatViewModel,
    uiStateDispatcher: UiStateDispatcher
) {
    val chatUiState by chatViewModel.chatUiState.collectAsState()
    val uiState: UiState by uiStateDispatcher.uiState.collectAsState()
    val isCheckMessageMode: Boolean = uiState.isCheckMessagesMode

    TopAppBar(
        title = {
            if (!isCheckMessageMode) InterlocutorDetails(
                chatViewModel = chatViewModel,
                navController = navController
            )
        },
        navigationIcon = {
            if (isCheckMessageMode)
                IconButton(onClick = { uiStateDispatcher.unsetCheckMessagesMode() }) {
                    Icon(imageVector = Icons.Rounded.Close, contentDescription = "off check messages btn")
                }
            else IconButton(onClick = { navController.popBackStack() }) {
                Icon(
                    imageVector = Icons.AutoMirrored.Rounded.KeyboardArrowLeft,
                    contentDescription = stringResource(id = R.string.btn_description_back),
                    modifier = Modifier.size(32.dp)
                )
            }
        },
        actions = {
            if (isCheckMessageMode)
                ChatTopBarCheckMessagesActions(
                    chatViewModel = chatViewModel,
                    uiStateDispatcher = uiStateDispatcher
                )
            else ChatTopBarActions(
                navController = navController,
                chatState = chatUiState,
                chatViewModel = chatViewModel,
                uiStateDispatcher = uiStateDispatcher
            )
        }
    )
}

@Composable
private fun InterlocutorDetails(
    chatViewModel: ChatViewModel,
    navController: NavHostController
) {
    val context: Context = LocalContext.current
    val chatUiState: ChatUiState by chatViewModel.chatUiState.collectAsState()
    val interlocutor: User? = chatUiState.interlocutor
    val friendName = "${interlocutor?.firstName} ${interlocutor?.lastName}".trim()

    // TODO: implement user online logic
    var isOnline: Boolean by rememberSaveable { mutableStateOf(false) }
    var onlineIndicator: Int by rememberSaveable { mutableIntStateOf(R.drawable.offline_indicator) }
    var onlineIndicatorText: String by rememberSaveable {
        mutableStateOf(context.getString(R.string.online_indicator_user_offline))
    }

    val lastOnline: LocalDateTime = LocalDateTime.of(2024, Month.MAY, 7, 15, 0)
    val lastOnlineString = DateFormatter.getRelativeDate(lastOnline)

    LaunchedEffect(key1 = isOnline) {
        if (isOnline) {
            onlineIndicator = R.drawable.online_indicator
            onlineIndicatorText = context.getString(R.string.online_indicator_user_online)
        }
        else {
            onlineIndicator = R.drawable.offline_indicator
            onlineIndicatorText = context.getString(R.string.online_indicator_user_offline, lastOnlineString)
        }
    }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        modifier = Modifier
            .clip(RoundedCornerShape(5.dp))
            .clickable { onInterlocutorDetailsClick(interlocutor, navController) }
            .padding(horizontal = 10.dp)
    ) {
        CircularAvatar(
            modifier = Modifier.size(40.dp),
            imageUrl = interlocutor?.avatarUrl
        )

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
                    painter = painterResource(id = onlineIndicator),
                    contentDescription = "online indicator",
                    modifier = Modifier.size(16.dp)
                )
                Text(
                    text = onlineIndicatorText,
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
) = interlocutor?.let { navController.navigate(Route.Profile.getStringRouteWithNavArg(it.id.toString())) }