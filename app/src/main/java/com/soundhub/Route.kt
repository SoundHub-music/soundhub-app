package com.soundhub

import com.soundhub.utils.Constants

sealed class Route(var route: String) {
    data object Authentication: Route(route = Constants.AUTHENTICATION_ROUTE) {
        val withNavArg = "$route/{${Constants.POST_REGISTER_NAV_ARG}}"
        data object ChooseGenres: Route(route = Constants.CHOOSE_GENRES_ROUTE)
        data object ChooseArtists: Route(route = Constants.CHOOSE_ARTISTS_ROUTE)
        data object FillUserData: Route(route = Constants.FILL_DATA_REGISTRATION_ROUTE)
    }

    data class Profile(var navArg: String? = null): Route(route = Constants.PROFILE_ROUTE) {
        init {
            navArg?.let {
                route = Regex(Constants.NAV_ARG_REGEX)
                    .replace(route, navArg ?: "")
            }
        }
    }

    data object Postline: Route(route = Constants.POSTLINE_ROUTE)
    data object Music: Route(route = Constants.MUSIC_ROUTE) {
        data object NewOfTheWeek: Route(route = Constants.MUSIC_NEW_OF_THE_WEEK)
        data object NewOfTheMonth: Route(route = Constants.MUSIC_NEW_OF_THE_MONTH)
        data object RecommendMusic: Route(route = Constants.MUSIC_RECOMMENDATIONS)
    }
    data object FriendList: Route(route = Constants.FRIEND_LIST_ROUTE)
    data object Gallery: Route(route = Constants.GALLERY_ROUTE)

    data object Messenger: Route(route = Constants.MESSENGER_ROUTE) {
        data class Chat(var navArg: String? = null): Route(route = Constants.MESSENGER_CHAT_ROUTE) {
            init {
                navArg?.let {
                    this.route = Regex(Constants.NAV_ARG_REGEX)
                        .replace(this.route, navArg ?: "")
                }
            }
        }
    }

    data object Settings: Route(route = Constants.SETTINGS_ROUTE)
    data object Notifications: Route(route = Constants.NOTIFICATIONS_ROUTE)
    data object EditUserData: Route(route = Constants.EDIT_USER_DATA_ROUTE)
    data object CreatePost: Route(route = Constants.CREATE_POST_ROUTE)
}