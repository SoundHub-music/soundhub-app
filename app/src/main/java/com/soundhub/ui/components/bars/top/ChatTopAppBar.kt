package com.soundhub.ui.components.bars.top

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.KeyboardArrowLeft
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.net.toUri
import androidx.navigation.NavHostController
import com.soundhub.R
import com.soundhub.Route
import com.soundhub.data.model.User
import com.soundhub.ui.components.avatar.CircularAvatar
import com.soundhub.ui.messenger.chat.ChatUiState
import com.soundhub.ui.messenger.chat.ChatViewModel
import com.soundhub.ui.viewmodels.UiStateDispatcher
import java.time.LocalDateTime

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatTopAppBar(
    navController: NavHostController,
    chatViewModel: ChatViewModel,
    uiStateDispatcher: UiStateDispatcher
) {
    val chatUiState by chatViewModel.chatUiState.collectAsState()
    val isCheckMessageModeEnabled = chatUiState.isCheckMessageModeEnabled

    TopAppBar(
        title = {
            if (!isCheckMessageModeEnabled) InterlocutorDetails(chatViewModel, navController)
        },
        navigationIcon = {
            IconButton(onClick = {
                if (isCheckMessageModeEnabled) {
                    chatViewModel.unsetCheckMessagesMode()
                } else {
                    navController.popBackStack()
                }
            }) {
                Icon(
                    imageVector = if (isCheckMessageModeEnabled) Icons.Rounded.Close else Icons.AutoMirrored.Rounded.KeyboardArrowLeft,
                    contentDescription = stringResource(id = R.string.btn_description_back),
                    modifier = Modifier.size(28.dp)
                )
            }
        },
        actions = {
            if (isCheckMessageModeEnabled) {
                ChatTopBarCheckMessagesActions(chatViewModel, uiStateDispatcher)
            } else {
                ChatTopBarActions(navController, chatUiState, chatViewModel, uiStateDispatcher)
            }
        }
    )
}

@Composable
private fun InterlocutorDetails(
    chatViewModel: ChatViewModel,
    navController: NavHostController
) {
    val chatUiState: ChatUiState by chatViewModel.chatUiState.collectAsState()
    val interlocutor: User? = chatUiState.interlocutor

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        modifier = Modifier
            .clip(RoundedCornerShape(5.dp))
            .clickable {
                interlocutor?.let {
                    val route = Route.Profile.getStringRouteWithNavArg(it.id.toString())
                    navController.navigate(route)
                }
            }
            .padding(horizontal = 10.dp)
    ) {
        CircularAvatar(
            modifier = Modifier.size(40.dp),
            imageUri = interlocutor?.avatarUrl?.toUri()
        )
        InterlocutorFullNameWithOnlineIndicator(chatViewModel)
    }
}

@Composable
private fun InterlocutorFullNameWithOnlineIndicator(
    chatViewModel: ChatViewModel,

) {
    val context: Context = LocalContext.current
    val chatUiState: ChatUiState by chatViewModel.chatUiState.collectAsState()
    val interlocutor: User? = chatUiState.interlocutor
    val lastOnline: LocalDateTime? = interlocutor?.lastOnline

    val friendName: String = "${interlocutor?.firstName} ${interlocutor?.lastName}".trim()

    var isOnline: Boolean by rememberSaveable { mutableStateOf(interlocutor?.isOnline ?: false) }
    var onlineIndicator: Int by rememberSaveable { mutableIntStateOf(R.drawable.offline_indicator) }
    var onlineIndicatorText: String by rememberSaveable { mutableStateOf("") }

    LaunchedEffect(interlocutor) {
        isOnline = interlocutor?.isOnline ?: false
        chatViewModel.updateOnlineStatusIndicator(
            isOnline = isOnline,
            lastOnline = lastOnline,
            context = context
        ) { indicator, text ->
            onlineIndicator = indicator
            onlineIndicatorText = text
        }
    }

    Column {
        Text(
            text = friendName,
            fontWeight = FontWeight.ExtraBold,
            fontSize = 18.sp,
            lineHeight = 28.sp
        )
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(2.dp)
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