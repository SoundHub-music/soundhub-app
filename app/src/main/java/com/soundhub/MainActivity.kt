package com.soundhub

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.soundhub.ui.theme.SoundHubTheme
import com.soundhub.ui.login.LoginScreen
import com.soundhub.utils.Routes

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val isLoggedIn = mutableStateOf(false)

        setContent {
            SoundHubTheme {
                val navController = rememberNavController()
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    NavHost(
                        navController = navController,
                        startDestination = if (isLoggedIn.value) Routes.POSTlINE else Routes.AUTHENTICATION
                    ) {
                        composable(Routes.AUTHENTICATION) {
                            LoginScreen()
                        }
                        composable(Routes.POSTlINE) {
                            Text(text = "This is a postline page")
                        }
                    }
                }
            }
        }
    }
}
