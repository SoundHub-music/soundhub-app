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
import com.soundhub.ui.profile.components.sections.user_actions.UserNameWithDescription
import com.soundhub.utils.DateFormatter

@Composable
internal fun UserMainDataSection(profileOwner: User?) {
    Column(
        verticalArrangement = Arrangement.spacedBy(5.dp)
    ) {
        UserNameWithDescription(profileOwner)
        OnlineStatusBlock(profileOwner)
        UserLocationText(profileOwner)
    }
}

@Composable
private fun OnlineStatusBlock(profileOwner: User?) {
    val context: Context = LocalContext.current

    var indicatorColor: Int by rememberSaveable { mutableIntStateOf(R.color.offline_status) }
    var onlineIndicatorText: String by rememberSaveable {
        mutableStateOf(context.getString(R.string.online_indicator_user_offline))
    }

    LaunchedEffect(key1 = profileOwner?.isOnline) {
        if (profileOwner?.isOnline == true) {
            indicatorColor = R.color.online_status
            onlineIndicatorText = context.getString(R.string.online_indicator_user_online)
        }
        else {
            val lastOnlineText: String = profileOwner
                ?.lastOnline?.let {
                    DateFormatter.getRelativeDate(it).lowercase()
            } ?: ""

            indicatorColor = R.color.offline_status
            onlineIndicatorText = if (lastOnlineText.isNotEmpty()) context.getString(
                R.string.online_indicator_user_offline_with_time,
                lastOnlineText
            )
            else context.getString(R.string.online_indicator_user_offline)
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
                .background(colorResource(id = indicatorColor))
        )
        Text(
            text = onlineIndicatorText,
            fontWeight = FontWeight.Light
        )
    }
}

@Composable
private fun UserLocationText(user: User?) {
    val userLocation: String = getUserLocation(user?.city, user?.country)
    if (userLocation.isNotEmpty())
        Text(
            text = userLocation,
            fontWeight = FontWeight.Light,
            fontSize = 14.sp
        )
}

internal fun getUserLocation(city: String?, country: String?): String {
    return if (city.isNullOrEmpty() && country.isNullOrEmpty()) ""
    else if (country?.isNotEmpty() == true && city.isNullOrEmpty()) country
    else "$country, $city"
}
