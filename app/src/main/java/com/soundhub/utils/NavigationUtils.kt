package com.soundhub.utils

import android.os.Bundle
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import com.soundhub.Route
import com.soundhub.data.datastore.UserPreferences
import com.soundhub.data.model.User
import com.soundhub.ui.viewmodels.UiStateDispatcher
import com.soundhub.utils.constants.Constants

class NavigationUtils {
    companion object {
        fun onNavDestinationChangedListener(
            uiStateDispatcher: UiStateDispatcher,
            controller: NavController,
            destination: NavDestination,
            userCreds: UserPreferences?,
            userInstance: User?
        ) {
            val backStackEntry: NavBackStackEntry? = controller.currentBackStackEntry
            val navArguments: Bundle? = backStackEntry?.arguments
            val route: String? = destination.route

            navigateToPostLineIfUserCredsIsNotNull(
                route = route,
                userCreds = userCreds,
                userInstance = userInstance,
                controller = controller
            )

            setCurrentRouteState(
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
                && userInstance != null
            )
                controller.navigate(Route.PostLine.route)
        }

        private fun setCurrentRouteState(
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
}