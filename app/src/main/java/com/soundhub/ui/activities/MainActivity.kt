package com.soundhub.ui.activities

import android.Manifest.permission.POST_NOTIFICATIONS
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.core.app.ActivityCompat
import androidx.core.splashscreen.SplashScreen
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.soundhub.R
import com.soundhub.data.dao.UserDao
import com.soundhub.data.datastore.UserCredsStore
import com.soundhub.data.datastore.UserPreferences
import com.soundhub.data.repository.ChatRepository
import com.soundhub.data.websocket.WebSocketClient
import com.soundhub.services.MessengerAndroidService
import com.soundhub.ui.authentication.AuthenticationViewModel
import com.soundhub.ui.authentication.registration.RegistrationViewModel
import com.soundhub.ui.events.UiEvent
import com.soundhub.ui.home.HomeScreen
import com.soundhub.ui.messenger.MessengerViewModel
import com.soundhub.ui.notifications.NotificationViewModel
import com.soundhub.ui.states.UiState
import com.soundhub.ui.theme.SoundHubTheme
import com.soundhub.ui.viewmodels.SplashScreenViewModel
import com.soundhub.ui.viewmodels.UiStateDispatcher
import com.soundhub.utils.NavigationUtils
import com.soundhub.utils.constants.Constants
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val registrationViewModel: RegistrationViewModel by viewModels()
    private val splashScreenViewModel: SplashScreenViewModel by viewModels()
    private val authViewModel: AuthenticationViewModel by viewModels()
    private val messengerViewModel: MessengerViewModel by viewModels()
    private val uiStateDispatcher: UiStateDispatcher by viewModels()
    private val notificationViewModel: NotificationViewModel by viewModels()

    @Inject
    lateinit var chatRepository: ChatRepository
    @Inject
    lateinit var userCredsStore: UserCredsStore
    @Inject
    lateinit var userDao: UserDao
    @Inject
    lateinit var webSocketClient: WebSocketClient

    private lateinit var navController: NavHostController
    private lateinit var uiEventState: Flow<UiEvent>
    private lateinit var uiState: Flow<UiState>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initSplashScreen()
        startAndroidService(MessengerAndroidService::class.java)

        if (ActivityCompat.checkSelfPermission(this, POST_NOTIFICATIONS) == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(this, arrayOf(POST_NOTIFICATIONS), 1);
        }

        uiEventState = uiStateDispatcher.uiEvent
        uiState = uiStateDispatcher.uiState

        lifecycleScope.launch {
            uiEventState.collect { event -> handleUiEvent(event, navController) }
        }

        setContent {
            SoundHubTheme(dynamicColor = false) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.surface
                ) {
                    NavigationListener()
                    HomeScreen(
                        navController = navController,
                        authViewModel = authViewModel,
                        uiStateDispatcher = uiStateDispatcher,
                        messengerViewModel = messengerViewModel,
                        registrationViewModel = registrationViewModel,
                        notificationViewModel = notificationViewModel
                    )
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("MainActivity", "onDestroy: user has closed the app")
        
        stopAndroidService(MessengerAndroidService::class.java)
        authViewModel.updateUserOnlineStatusDelayed(
            setOnline = false,
            delayTime = Constants.SET_OFFLINE_DELAY_ON_DESTROY
        )
        uiStateDispatcher.clearState()
    }

    override fun onStop() {
        super.onStop()
        Log.d("MainActivity", "onStop: user has minimized the app")
        authViewModel.updateUserOnlineStatusDelayed(
            setOnline = false,
            delayTime = Constants.SET_OFFLINE_DELAY_ON_STOP
        )
    }

    override fun onResume() {
        super.onResume()
        Log.d("MainActivity", "onResume: user has opened the app")
        authViewModel.updateUserOnlineStatusDelayed(setOnline = true)
    }

    @Composable
    private fun NavigationListener() {
        val coroutineScope = rememberCoroutineScope()
        val userCreds: Flow<UserPreferences> = authViewModel.userCreds
        val uiState: UiState by uiStateDispatcher.uiState.collectAsState(initial = UiState())

        navController = rememberNavController()
        navController.addOnDestinationChangedListener { controller, destination, _ ->
            coroutineScope.launch {
                NavigationUtils.onNavDestinationChangedListener(
                    controller = controller,
                    destination = destination,
                    userCreds = userCreds.firstOrNull(),
                    userInstance = uiState.authorizedUser,
                    uiStateDispatcher = uiStateDispatcher
                )
            }
        }
    }

    private fun initSplashScreen() {
        val splashScreen: SplashScreen = installSplashScreen()
        splashScreen.setKeepOnScreenCondition { splashScreenViewModel.isLoading.value }
    }

    private fun <K> startAndroidService(clazz: Class<K>) {
        val intent = Intent(this, clazz)
        startService(intent)
    }

    private fun <K> stopAndroidService(clazz: Class<K>) {
        val intent = Intent(this, clazz)
        stopService(intent)
    }

    private fun handleUiEvent(event: UiEvent, navController: NavHostController) = lifecycleScope.launch {
        Log.d("MainActivity", "handleUiEvent: $event")
        when (event) {
            is UiEvent.ShowToast -> {
                val uiText: String = event.uiText.getString(this@MainActivity)
                if (uiText.isNotEmpty())
                    Toast.makeText(
                        this@MainActivity,
                        uiText,
                        Toast.LENGTH_SHORT
                    ).show()
            }

            is UiEvent.Navigate -> navController.navigate(event.route.route)
            is UiEvent.PopBackStack -> navController.popBackStack()
            is UiEvent.SearchButtonClick -> uiStateDispatcher.toggleSearchBarActive()
            is UiEvent.Error -> {
                val message: String = event.response.detail
                    ?: event.throwable?.localizedMessage
                    ?: event.customMessageStringRes?.let { getString(it) }
                    ?: getString(R.string.toast_common_error)

                if (event.throwable !is CancellationException)
                    Toast.makeText(this@MainActivity, message, Toast.LENGTH_LONG).show()
            }
        }
    }
}