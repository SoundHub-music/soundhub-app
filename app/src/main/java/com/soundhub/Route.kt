package com.soundhub

import com.soundhub.utils.Constants

sealed class Route(var route: String) {
    object Authentication: Route(route = Constants.AUTHENTICATION_ROUTE) {
        val withNavArg = "$route/{${Constants.POST_REGISTER_NAV_ARG}}"
        object ChooseGenres: Route(route = Constants.CHOOSE_GENRES_ROUTE)
        object ChooseArtists: Route(route = Constants.CHOOSE_ARTISTS_ROUTE)
        object FillUserData: Route(route = Constants.FILL_DATA_REGISTRATION_ROUTE)
    }

    data class Profile(var navArg: String? = null): Route(route = Constants.PROFILE_ROUTE) {
        init {
            if (navArg != null) {
                route = Regex(Constants.NAV_ARG_REGEX).replace(route, navArg ?: "")
            }
        }
    }

    object Postline: Route(route = Constants.POSTLINE_ROUTE)
    object Music: Route(route = Constants.MUSIC_ROUTE)
    object FriendList: Route(route = Constants.FRIEND_LIST_ROUTE)
    object Gallery: Route(route = Constants.GALLERY_ROUTE)

    object Messenger: Route(route = Constants.MESSENGER_ROUTE) {
        data class Chat(var navArg: String? = null): Route(route = Constants.MESSENGER_CHAT_ROUTE) {
            init {
                if (navArg != null) this.route = Regex(Constants.NAV_ARG_REGEX).replace(this.route, navArg ?: "")
            }
        }
    }

    object Settings: Route(route = Constants.SETTINGS_ROUTE)
    object Notifications: Route(route = Constants.NOTIFICATIONS_ROUTE)
    object EditUserData: Route(route = Constants.EDIT_USER_DATA_ROUTE)
    object CreatePost: Route(route = Constants.CREATE_POST_ROUTE)
}