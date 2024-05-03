package com.soundhub.ui.profile.components.sections.user_main_data

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.soundhub.data.model.User
import com.soundhub.ui.profile.components.sections.user_actions.UserNameWithDescription

@Composable
internal fun UserMainDataSection(user: User?) {
    Column {
        UserNameWithDescription(user)
        UserLocationText(user)
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
    return if ((city == null && country == null) || (city!!.isEmpty() && country!!.isEmpty())) ""
    else if (country!!.isNotEmpty() && city.isEmpty()) country
    else "$country, $city"
}
