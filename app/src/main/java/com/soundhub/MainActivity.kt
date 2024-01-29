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
    private val uiEventDispatcher: UiEventDispatcher by viewModels()
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

                    LaunchedEffect(key1 = uiEventDispatcher.uiEvent) {
                        uiEventDispatcher.uiEvent.collect {
                            event ->
                            Log.d(Constants.LOG_CURRENT_EVENT_TAG, "MainActivity[onCreate]: $event")
                            when(event) {
                                is UiEvent.ShowToast -> Toast.makeText(context, event.message, Toast.LENGTH_SHORT).show()
                                is UiEvent.Navigate -> navController.navigate(event.route.route)
                                else -> Unit
                            }
                        }
                    }

                    HomeScreen(navController, uiEventDispatcher)
                }
            }
        }
    }
}
