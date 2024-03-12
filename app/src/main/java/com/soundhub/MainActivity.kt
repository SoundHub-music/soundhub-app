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
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.splashscreen.SplashScreen
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.soundhub.ui.authentication.AuthenticationViewModel
import com.soundhub.ui.authentication.postregistration.RegistrationViewModel
import com.soundhub.ui.theme.SoundHubTheme
import com.soundhub.ui.home.HomeScreen
import com.soundhub.ui.messenger.MessengerViewModel
import com.soundhub.ui.messenger.chat.ChatViewModel
import com.soundhub.utils.Constants
import com.soundhub.ui.viewmodels.SplashScreenViewModel
import com.soundhub.ui.viewmodels.UiStateDispatcher
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val registrationViewModel: RegistrationViewModel by viewModels()
    private val splashScreenViewModel: SplashScreenViewModel by viewModels()
    private val authViewModel: AuthenticationViewModel by viewModels()
    private val messengerViewModel: MessengerViewModel by viewModels()
    private val uiStateDispatcher: UiStateDispatcher by viewModels()
    private val chatViewModel: ChatViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val splashScreen: SplashScreen = installSplashScreen()
        splashScreen.setKeepOnScreenCondition{ splashScreenViewModel.isLoading.value }

        setContent {
            SoundHubTheme(dynamicColor = false) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.surface
                ) {
                    val navController: NavHostController = rememberNavController()
                    val context = LocalContext.current
                    val uiEventState = uiStateDispatcher.uiEvent

                    navController.addOnDestinationChangedListener { _, _, _ ->
                        uiStateDispatcher.setSearchBarActive(false)
                    }

                    LaunchedEffect(key1 = uiEventState) {
                        uiEventState.collect { event ->
                            Log.d(Constants.LOG_CURRENT_EVENT_TAG, "MainActivity[onCreate]: $event")
                            when (event) {
                                is UiEvent.ShowToast -> Toast.makeText(
                                    context,
                                    event.uiText.getString(context),
                                    Toast.LENGTH_SHORT
                                ).show()

                                is UiEvent.Navigate -> {
                                    Log.d(
                                        Constants.LOG_CURRENT_ROUTE,
                                        "MainActivity[onCreate]: ${event.route.route}"
                                    )
                                    navController.navigate(event.route.route)
                                }

                                is UiEvent.PopBackStack -> navController.popBackStack()
                                is UiEvent.SearchButtonClick -> uiStateDispatcher.toggleSearchBarActive()
                                is UiEvent.UpdateCurrentUser -> authViewModel.setCurrentUser(event.user)
                                else -> Unit
                            }
                        }
                    }

                    HomeScreen(
                        navController = navController,
                        authViewModel = authViewModel,
                        uiStateDispatcher = uiStateDispatcher,
                        chatViewModel = chatViewModel,
                        messengerViewModel = messengerViewModel,
                        registrationViewModel = registrationViewModel
                    )
                }
            }
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
