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
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.soundhub.ui.authentication.AuthenticationViewModel
import com.soundhub.ui.theme.SoundHubTheme
import com.soundhub.ui.home.HomeScreen
import com.soundhub.utils.Constants
import com.soundhub.utils.UiEvent
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val uiStateDispatcher: UiStateDispatcher by viewModels()
    private val authViewModel: AuthenticationViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SoundHubTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.surface
                ) {
                    val navController: NavHostController = rememberNavController()
                    val context = LocalContext.current

                    navController.addOnDestinationChangedListener { _, _, _ ->
                        uiStateDispatcher.setSearchBarActive(false)
                    }

                    LaunchedEffect(key1 = authViewModel.registerState) {
                        authViewModel.registerState.collect {
                            Log.d("register_state", authViewModel.registerState.value.toString())
                        }
                    }

                    LaunchedEffect(key1 = uiStateDispatcher.uiEvent) {
                        uiStateDispatcher.uiEvent.collect { event ->
                            Log.d(Constants.LOG_CURRENT_EVENT_TAG, "MainActivity[onCreate]: $event")
                            when (event) {
                                is UiEvent.ShowToast -> Toast.makeText(
                                    context,
                                    event.message,
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
                                else -> Unit
                            }
                        }
                    }

                    HomeScreen(
                        navController = navController,
                        authViewModel = authViewModel,
                        uiStateDispatcher = uiStateDispatcher
                    )
                }
            }
        }
    }
}
