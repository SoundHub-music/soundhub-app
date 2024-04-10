package com.soundhub

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.soundhub.ui.authentication.AuthenticationViewModel
import com.soundhub.ui.authentication.postregistration.RegistrationViewModel
import com.soundhub.ui.edit_profile.EditUserProfileViewModel
import com.soundhub.ui.theme.SoundHubTheme
import com.soundhub.ui.home.HomeScreen
import com.soundhub.ui.messenger.MessengerViewModel
import com.soundhub.ui.messenger.chat.ChatViewModel
import com.soundhub.utils.Constants
import com.soundhub.ui.viewmodels.SplashScreenViewModel
import com.soundhub.ui.viewmodels.UiStateDispatcher
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val registrationViewModel: RegistrationViewModel by viewModels()
    private val splashScreenViewModel: SplashScreenViewModel by viewModels()
    private val authViewModel: AuthenticationViewModel by viewModels()
    private val messengerViewModel: MessengerViewModel by viewModels()
    private val uiStateDispatcher: UiStateDispatcher by viewModels()
    private val chatViewModel: ChatViewModel by viewModels()
    private val editUserProfileViewModel: EditUserProfileViewModel by viewModels()

    private lateinit var navController: NavHostController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val splashScreen: SplashScreen = installSplashScreen()
        splashScreen.setKeepOnScreenCondition{ splashScreenViewModel.isLoading.value }

        val uiEventState = uiStateDispatcher.uiEvent

        lifecycleScope.launch {
            uiEventState.collect { event ->
                handleUiEvent(event, navController)
            }
        }

        setContent {
            SoundHubTheme(dynamicColor = false) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.surface
                ) {
                    navController = rememberNavController()
                    navController.addOnDestinationChangedListener { controller, destination, _ ->
                        val backStackEntry: NavBackStackEntry? = controller.currentBackStackEntry
                        val navArguments: Bundle? = backStackEntry?.arguments
                        var argument: String?
                        var route: String? = destination.route

                        navArguments?.let { args ->
                            argument = args.getString(Constants.PROFILE_NAV_ARG)
                                ?: args.getString(Constants.CHAT_NAV_ARG)
                                ?: ""
                            route = route?.replace(Regex("\\{([^{}]+)\\}"), argument!!)
                        }

                        uiStateDispatcher.setCurrentRoute(route)
                        uiStateDispatcher.setSearchBarActive(false)
                    }

                    val currentRoute by uiStateDispatcher.currentRoute.collectAsState()

                    LaunchedEffect(key1 = currentRoute) {
                        Log.d("MainActivity", "current route: $currentRoute")
                    }

                    HomeScreen(
                        navController = navController,
                        authViewModel = authViewModel,
                        uiStateDispatcher = uiStateDispatcher,
                        chatViewModel = chatViewModel,
                        messengerViewModel = messengerViewModel,
                        registrationViewModel = registrationViewModel,
                        editUserProfileViewModel = editUserProfileViewModel
                    )
                }
            }
        }
    }

    private fun handleUiEvent(event: UiEvent, navController: NavHostController) {
        Log.d(Constants.LOG_CURRENT_EVENT_TAG, "MainActivity[onCreate]: $event")
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
