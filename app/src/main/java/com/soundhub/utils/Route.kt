package com.soundhub.utils

sealed class Route(val route: String) {
    object Authentication: Route(route = "authentication") {
        object ChooseGenres: Route(route = "${Authentication.route}/choose-genres")
        object ChooseArtists: Route(route = "${Authentication.route}/choose-artists")
        object FillUserData: Route(route = "${Authentication.route}/fill_user_data")
    }


    object Profile: Route(route = "profile")
    object Postline: Route(route = "postline")
    object Music: Route(route = "music")
    object Messenger: Route(route = "messenger")
    object Settings: Route(route = "settings")
    object Notifications: Route(route = "notifications")
    object EditUserData: Route(route = "edit_user_data")
    
    companion object {
        fun valueOf(route: String?): Route? {
            return when (route) {
                Authentication.route -> Authentication
                Authentication.ChooseArtists.route -> Authentication.ChooseArtists
                Authentication.ChooseGenres.route -> Authentication.ChooseGenres
                Authentication.FillUserData.route -> Authentication.FillUserData
                Notifications.route -> Notifications
                EditUserData.route -> EditUserData
                Messenger.route -> Messenger
                Postline.route -> Postline
                Settings.route -> Settings
                Profile.route -> Profile
                Music.route -> Music
                else -> null
            }
        }
    }
}