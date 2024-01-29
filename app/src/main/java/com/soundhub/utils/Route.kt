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
    
    companion object {
        fun valueOf(route: String?): Route? {
            return when (route) {
                Authentication.route -> Authentication
                Authentication.ChooseArtists.route -> Authentication.ChooseArtists
                Authentication.ChooseGenres.route -> Authentication.ChooseGenres
                Authentication.FillUserData.route -> Authentication.FillUserData
                Profile.route -> Profile
                Postline.route -> Postline
                Music.route -> Music
                Messenger.route -> Messenger
                Settings.route -> Settings
                else -> null
            }
        }
    }
}