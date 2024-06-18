package com.soundhub.ui.profile.components.sections.user_main_data

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
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
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.soundhub.R
import com.soundhub.data.model.User
import com.soundhub.ui.profile.ProfileUiState
import com.soundhub.ui.profile.ProfileViewModel
import com.soundhub.ui.profile.components.sections.user_actions.UserNameWithDescription
import com.soundhub.utils.UserUtils

@Composable
internal fun UserMainDataSection(profileViewModel: ProfileViewModel) {
    Column(
        verticalArrangement = Arrangement.spacedBy(5.dp)
    ) {
        UserNameWithDescription(profileViewModel)
        OnlineStatusBlock(profileViewModel)
        UserLocationText(profileViewModel)
    }
}

@Composable
private fun OnlineStatusBlock(profileViewModel: ProfileViewModel) {
    val profileUiState: ProfileUiState by profileViewModel
        .profileUiState
        .collectAsState()

    val profileOwner: User? = profileUiState.profileOwner
    val context: Context = LocalContext.current

    var indicatorColorState: Int by rememberSaveable { mutableIntStateOf(R.color.offline_status) }
    var onlineIndicatorTextState: String by rememberSaveable {
        mutableStateOf(context.getString(R.string.online_indicator_user_offline))
    }

    LaunchedEffect(key1 = profileOwner?.isOnline) {
        UserUtils.updateOnlineStatusIndicator(
            context = context,
            user = profileOwner
        ) { _, indicatorColor , onlineIndicatorText ->
            indicatorColorState = indicatorColor
            onlineIndicatorTextState = onlineIndicatorText
        }
    }

    Row(
        horizontalArrangement = Arrangement.spacedBy(5.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(8.dp)
                .clip(CircleShape)
                .background(colorResource(id = indicatorColorState))
        )
        Text(
            text = onlineIndicatorTextState,
            fontWeight = FontWeight.Light
        )
    }
}

@Composable
private fun UserLocationText(profileViewModel: ProfileViewModel) {
    val profileUiState: ProfileUiState by profileViewModel
        .profileUiState
        .collectAsState()
    val profileOwner: User? = profileUiState.profileOwner
    val userLocation: String = rememberSaveable(profileOwner) {
        UserUtils.getUserLocation(profileOwner?.city, profileOwner?.country)
    }

    if (userLocation.isNotEmpty())
        Text(
            text = userLocation,
            fontWeight = FontWeight.Light,
            fontSize = 14.sp
        )
}