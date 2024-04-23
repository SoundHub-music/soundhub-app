package com.soundhub.ui.activities

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.asLiveData
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.soundhub.Route
import com.soundhub.data.datastore.UserPreferences
import com.soundhub.data.model.User
import com.soundhub.ui.events.UiEvent
import com.soundhub.ui.authentication.AuthenticationViewModel
import com.soundhub.ui.authentication.postregistration.RegistrationViewModel
import com.soundhub.ui.edit_profile.EditUserProfileViewModel
import com.soundhub.ui.theme.SoundHubTheme
import com.soundhub.ui.home.HomeScreen
import com.soundhub.ui.messenger.MessengerViewModel
import com.soundhub.utils.Constants
import com.soundhub.ui.viewmodels.SplashScreenViewModel
import com.soundhub.ui.viewmodels.UiStateDispatcher
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val registrationViewModel: RegistrationViewModel by viewModels()
    private val splashScreenViewModel: SplashScreenViewModel by viewModels()
    private val authViewModel: AuthenticationViewModel by viewModels()
    private val messengerViewModel: MessengerViewModel by viewModels()
    private val uiStateDispatcher: UiStateDispatcher by viewModels()
    private val editUserProfileViewModel: EditUserProfileViewModel by viewModels()

    private lateinit var navController: NavHostController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initSplashScreen()
        val uiEventState = uiStateDispatcher.uiEvent.asLiveData()

        lifecycleScope.launch {
            uiEventState.observe(this@MainActivity) { event ->
                handleUiEvent(event, navController)
            }
        }

        setContent {
            SoundHubTheme(dynamicColor = false) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.surface
                ) {
                    val coroutineScope = rememberCoroutineScope()
                    navController = rememberNavController()
                    navController
                        .addOnDestinationChangedListener { controller, destination, bundle ->
                            run {
                                coroutineScope.launch {
                                    onNavDestinationChangeListener(
                                        controller = controller,
                                        destination = destination,
                                        bundle = bundle,
                                        userCreds = authViewModel.userCreds.firstOrNull(),
                                        userInstance = authViewModel.userInstance.value.current
                                    )
                                }
                            }
                        }

                    HomeScreen(
                        navController = navController,
                        authViewModel = authViewModel,
                        uiStateDispatcher = uiStateDispatcher,
                        messengerViewModel = messengerViewModel,
                        registrationViewModel = registrationViewModel,
                        editUserProfileViewModel = editUserProfileViewModel,
                    )
                }
            }
        }
    }

    private fun initSplashScreen() {
        val splashScreen: SplashScreen = installSplashScreen()
        splashScreen.setKeepOnScreenCondition{ splashScreenViewModel.isLoading.value }
    }

    private fun onNavDestinationChangeListener(
        controller: NavController,
        destination: NavDestination,
        bundle: Bundle?,
        userCreds: UserPreferences?,
        userInstance: User?
    ) {
        val backStackEntry: NavBackStackEntry? = controller.currentBackStackEntry
        val navArguments: Bundle? = backStackEntry?.arguments
        var route: String? = destination.route

        // it doesn't allow to navigate to auth screen if there is access token or user instance
        if (route?.contains(Route.Authentication.route) == true
            && !userCreds?.accessToken.isNullOrEmpty()
            && userInstance != null
        )
            controller.navigate(Route.Postline.route)

        navArguments?.let {  args ->
            route = transformRawRouteWithArguments(
                argument = args.getString(Constants.PROFILE_NAV_ARG)
                    ?: args.getString(Constants.CHAT_NAV_ARG)
                    ?: "",
                route = route
            )
        }

        uiStateDispatcher.setCurrentRoute(route)
        uiStateDispatcher.setSearchBarActive(false)
    }

    private fun transformRawRouteWithArguments(
        argument: String,
        route: String?
    ): String? = route?.replace(Regex("\\{([^{}]+)\\}"), argument)

    private fun handleUiEvent(event: UiEvent, navController: NavHostController) {
        Log.d("MainActivity", "handleUiEvent: $event")
        when (event) {
            is UiEvent.ShowToast -> {
                if (event.uiText.getString(this).isNotEmpty())
                    Toast.makeText(
                        this, event.uiText.getString(this), Toast.LENGTH_SHORT
                    ).show()
            }

            is UiEvent.Navigate -> navController
                .navigate(event.route.route)
            is UiEvent.PopBackStack -> navController
                .popBackStack()
            is UiEvent.SearchButtonClick -> uiStateDispatcher
                .toggleSearchBarActive()
            is UiEvent.UpdateUser -> editUserProfileViewModel
                .updateUser()
            is UiEvent.UpdateUserInstance -> authViewModel
                .setCurrentUser(event.user)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("MainActivity", "onDestroy: user has closed the app")
        // TODO: implement setting offline user status
    }

    override fun onStop() {
        super.onStop()
        Log.d("MainActivity", "onStop: user has minimized the app")
        // TODO: implement setting offline user status after a certain time
    }
}
