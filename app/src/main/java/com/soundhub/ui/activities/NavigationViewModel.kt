package com.soundhub.ui.activities

import android.os.Bundle
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import com.soundhub.Route
import com.soundhub.data.datastore.UserCredsStore
import com.soundhub.data.datastore.UserPreferences
import com.soundhub.data.model.User
import com.soundhub.ui.viewmodels.UiStateDispatcher
import com.soundhub.utils.constants.Constants
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@HiltViewModel
class NavigationViewModel @Inject constructor(
    private val uiStateDispatcher: UiStateDispatcher,
    userCredsStore: UserCredsStore
): ViewModel() {
    private val userCreds = userCredsStore.getCreds()

    suspend fun onNavDestinationChangedListener(
        controller: NavController,
        destination: NavDestination,
    ) {
        val userInstance = uiStateDispatcher.uiState.map { it.authorizedUser }.firstOrNull()
        val userCreds = userCreds.firstOrNull()
        val backStackEntry: NavBackStackEntry? = controller.currentBackStackEntry
        val navArguments: Bundle? = backStackEntry?.arguments
        val route: String? = destination.route

        Log.d("NavigationUtils", "onNavDestinationChangedListener[current_route]: $route")

        navigateToPostLineIfUserCredsIsNotNull(
            route = route,
            userCreds = userCreds,
            userInstance = userInstance,
            controller = controller
        )

        updateCurrentRouteState(
            appBundleState = navArguments,
            uiStateDispatcher = uiStateDispatcher,
            route = route
        )
        uiStateDispatcher.setSearchBarActive(false)
    }

    private fun navigateToPostLineIfUserCredsIsNotNull(
        route: String?,
        userCreds: UserPreferences?,
        userInstance: User?,
        controller: NavController
    ) {
        // it doesn't allow to navigate to the auth screen if there are access token and user instance
        if (route?.contains(Route.Authentication.route) == true
            && !userCreds?.accessToken.isNullOrEmpty()
            && !userCreds?.refreshToken.isNullOrEmpty()
            && userInstance != null
        )
            controller.navigate(Route.PostLine.route)
    }

    private fun updateCurrentRouteState(
        appBundleState: Bundle?,
        route: String?,
        uiStateDispatcher: UiStateDispatcher
    ) {
        var currentRoute = route
        val argument: String? = appBundleState
            ?.keySet()
            ?.firstOrNull { it in Constants.ALL_NAV_ARGS }

        appBundleState?.let { bundle ->
            currentRoute = Route.replaceNavArgTemplate(
                navArg = bundle.getString(argument) ?: "",
                route = route ?: ""
            )
        }

        uiStateDispatcher.setCurrentRoute(currentRoute)
    }
}