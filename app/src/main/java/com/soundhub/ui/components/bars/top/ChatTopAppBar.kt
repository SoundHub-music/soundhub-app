package com.soundhub.ui.components.bars.top

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.KeyboardArrowLeft
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.soundhub.R
import com.soundhub.ui.components.CircularUserAvatar
import com.soundhub.ui.messenger_chat.ChatViewModel
import java.time.LocalDateTime

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatTopAppBar(
    navController: NavHostController,
    chatViewModel: ChatViewModel = hiltViewModel()
) {
    TopAppBar(
        modifier = Modifier.padding(5.dp),
        title = { InterlocutorDetails() },
        navigationIcon = {
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(
                    imageVector = Icons.AutoMirrored.Rounded.KeyboardArrowLeft,
                    contentDescription = stringResource(id = R.string.btn_description_back),
                    modifier = Modifier.size(32.dp)
                )
            }
        },
        actions = { TopBarActions(navController = navController) }
    )
}

@Composable
private fun InterlocutorDetails() {
    // temporary variables
    val friendName = "Alex Merser"
    val lastOnline = LocalDateTime.of(2024, 2, 10, 15, 10)
    val isOnline = false
    val indicator = painterResource(
        id = if (isOnline) R.drawable.online_indicator
        else R.drawable.offline_indicator
    )

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        modifier = Modifier.padding(end = 10.dp)
    ) {
        CircularUserAvatar(chatItem = null)

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
                    contentDescription = null,
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