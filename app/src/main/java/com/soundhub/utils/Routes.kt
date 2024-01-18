package com.soundhub.utils

sealed class Routes(val route: String, val pageName: String? = null) {
    object Authentication: Routes(route = "authentication")
    object Profile: Routes(route = "profile")
    object Postline: Routes(route = "postline")
    object Music: Routes(route = "music")
    object Messenger: Routes(route = "messenger")
    object Settings: Routes(route = "settings")
}