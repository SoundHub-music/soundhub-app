package com.soundhub.utils

sealed class Routes(val route: String, val pageName: String? = null) {
    object AppStart: Routes(route = "appStart")
    object Authenticated: Routes(route = "authenticatedScreen")
    object Authentication: Routes(route = "authentication")
    object Profile: Routes(route = "profile")
    object Postline: Routes(route = "postline")
    object Music: Routes(route = "music")
    object Messenger: Routes(route = "messenger")
    object Settings: Routes(route = "settings")
}