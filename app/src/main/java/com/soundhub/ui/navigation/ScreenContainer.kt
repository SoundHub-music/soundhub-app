package com.soundhub.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import com.soundhub.Route
import com.soundhub.data.datastore.UserPreferences

@Composable
internal fun ScreenContainer(
    userCreds: UserPreferences?,
    navController: NavHostController,
    screen: @Composable () -> Unit = {}
) {
    if (userCreds?.accessToken != null) screen()
    else navController.navigate(Route.Authentication.route)
}