package com.soundhub.ui.activities

import android.Manifest
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.ServiceConnection
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
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
import com.soundhub.services.MessengerAndroidService
import com.soundhub.ui.events.UiEvent
import com.soundhub.ui.layout.RootLayout
import com.soundhub.ui.pages.authentication.AuthenticationViewModel
import com.soundhub.ui.pages.authentication.registration.RegistrationViewModel
import com.soundhub.ui.pages.messenger.MessengerViewModel
import com.soundhub.ui.pages.music.MusicViewModel
import com.soundhub.ui.pages.notifications.NotificationViewModel
import com.soundhub.ui.theme.SoundHubTheme
import com.soundhub.ui.viewmodels.NavigationViewModel
import com.soundhub.ui.viewmodels.SplashScreenViewModel
import com.soundhub.ui.viewmodels.UiStateDispatcher
import com.soundhub.utils.constants.Constants
import com.soundhub.utils.extensions.context_wrapper.registerReceiverExtended
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CancellationException
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
	private val navigationViewModel: NavigationViewModel by viewModels()
	private val musicViewModel: MusicViewModel by viewModels()

	@Inject
	lateinit var userDao: UserDao
	private lateinit var navController: NavHostController

	private lateinit var messengerAndroidService: MessengerAndroidService
	private var isMessengerConnected = false

	private val messengerConnection = object : ServiceConnection {
		override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
			val serviceBinder = service as MessengerAndroidService.LocalBinder
			messengerAndroidService = serviceBinder.getService()
			isMessengerConnected = true
		}

		override fun onServiceDisconnected(name: ComponentName?) {
			isMessengerConnected = false
		}
	}

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)

		val chatId = intent?.getStringExtra(Constants.CHAT_NAV_ARG)
		Log.d("MainActivity", chatId.toString())
		runActionsOnCreateActivity()

		setContent {
			SoundHubTheme(
				dynamicColor = false,
				darkTheme = uiStateDispatcher.getBooleanThemeValue()
			) {
				Surface(
					modifier = Modifier.fillMaxSize(),
					color = MaterialTheme.colorScheme.surface
				) {
					NavigationListener()
					RootLayout(
						navController = navController,
						authViewModel = authViewModel,
						uiStateDispatcher = uiStateDispatcher,
						messengerViewModel = messengerViewModel,
						registrationViewModel = registrationViewModel,
						notificationViewModel = notificationViewModel,
						musicViewModel = musicViewModel
					)
				}
			}
		}
	}

	override fun onDestroy() {
		super.onDestroy()
		Log.d("MainActivity", "onDestroy: user has closed the app")

		uiStateDispatcher.clearState()
		stopAndroidService(MessengerAndroidService::class.java)
		authViewModel.updateUserOnlineStatusDelayed(
			online = false,
			delayTime = Constants.SET_OFFLINE_DELAY_ON_DESTROY
		)
		if (isMessengerConnected) {
			messengerAndroidService.unbindService(messengerConnection)
			isMessengerConnected = false
		}
	}

	override fun onStop() {
		super.onStop()
		Log.d("MainActivity", "onStop: user has minimized the app")
		authViewModel.updateUserOnlineStatusDelayed(
			online = false,
			delayTime = Constants.SET_OFFLINE_DELAY_ON_STOP
		)
	}

	override fun onResume() {
		super.onResume()
		Log.d("MainActivity", "onResume: user has opened the app")
		authViewModel.updateUserOnlineStatusDelayed(online = true)
	}

	override fun onStart() {
		super.onStart()
		Intent(this, MessengerAndroidService::class.java).also { intent ->
			if (isMessengerConnected)
				messengerAndroidService.bindService(
					intent,
					messengerConnection,
					Context.BIND_AUTO_CREATE
				)
		}
	}

	private suspend fun observeUiEvents() {
		uiStateDispatcher.uiEvent.collect { event -> handleUiEvent(event, navController) }
	}

	private fun runActionsOnCreateActivity() {
		registerReceivers()
		initSplashScreen()
		startAndroidService(MessengerAndroidService::class.java)
		requestPermissions()

		lifecycleScope.launch {
			launch { observeUiEvents() }
		}
	}

	@Composable
	private fun NavigationListener() {
		val coroutineScope = rememberCoroutineScope()

		navController = rememberNavController()
		navController.addOnDestinationChangedListener { controller, destination, _ ->
			coroutineScope.launch {
				navigationViewModel.onNavDestinationChangedListener(
					controller = controller,
					destination = destination,
				)
			}
		}
	}

	private fun registerReceivers() {
		registerReceiverExtended(
			messengerViewModel.getMessageReceiver(),
			IntentFilter(MessengerAndroidService.MESSAGE_RECEIVER),

		)

		registerReceiverExtended(
			messengerViewModel.getReadMessageReceiver(),
			IntentFilter(MessengerAndroidService.READ_MESSAGE_RECEIVER)
		)

		registerReceiverExtended(
			messengerViewModel.getDeletedMessageReceiver(),
			IntentFilter(MessengerAndroidService.DELETE_MESSAGE_RECEIVER)
		)
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

	private fun requestPermissions() {
		if (ActivityCompat.checkSelfPermission(
				this,
				Manifest.permission.POST_NOTIFICATIONS
			) != PackageManager.PERMISSION_GRANTED
		) {
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
				val permissions = arrayOf(Manifest.permission.POST_NOTIFICATIONS)
				ActivityCompat.requestPermissions(this, permissions,101)
			}
		}
	}

	private fun handleUiEvent(event: UiEvent, navController: NavHostController) {
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

				if (event.throwable !is CancellationException && message.isNotEmpty())
					Toast.makeText(this@MainActivity, message, Toast.LENGTH_LONG).show()
			}

			is UiEvent.UpdateUserInstance -> lifecycleScope.launch {
				authViewModel.initializeUser()
			}
		}
	}
}