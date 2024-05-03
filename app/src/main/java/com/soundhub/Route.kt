package com.soundhub

import com.soundhub.utils.constants.Constants

sealed class Route(var route: String): Cloneable {
    data object Authentication: Route(route = Constants.AUTHENTICATION_ROUTE) {
        val withNavArg = "$route/{${Constants.POST_REGISTER_NAV_ARG}}"
        data object ChooseGenres: Route(route = Constants.CHOOSE_GENRES_ROUTE)
        data object ChooseArtists: Route(route = Constants.CHOOSE_ARTISTS_ROUTE)
        data object FillUserData: Route(route = Constants.FILL_DATA_REGISTRATION_ROUTE)
    }

    data object Profile: Route(route = Constants.PROFILE_ROUTE)

    data object Postline: Route(route = Constants.POSTLINE_ROUTE)
    data object Music: Route(route = Constants.MUSIC_ROUTE) {
        data object NewOfTheWeek: Route(route = Constants.MUSIC_NEW_OF_THE_WEEK)
        data object NewOfTheMonth: Route(route = Constants.MUSIC_NEW_OF_THE_MONTH)
        data object RecommendMusic: Route(route = Constants.MUSIC_RECOMMENDATIONS)
    }

    data object FriendList: Route(route = Constants.FRIEND_LIST_ROUTE)
    data object Gallery: Route(route = Constants.GALLERY_ROUTE)

    data object Messenger: Route(route = Constants.MESSENGER_ROUTE) {
        data object Chat: Route(route = Constants.MESSENGER_CHAT_ROUTE)
    }

    data object Settings: Route(route = Constants.SETTINGS_ROUTE)
    data object Notifications: Route(route = Constants.NOTIFICATIONS_ROUTE)
    data object EditUserData: Route(route = Constants.EDIT_USER_DATA_ROUTE)
    data object EditFavoriteArtists: Route(route = Constants.EDIT_FAV_ARTISTS_ROUTE)
    data object EditFavoriteGenres: Route(route = Constants.EDIT_FAV_GENRES_ROUTE)
    data object PostEditor: Route(route = Constants.POST_EDITOR_ROUTE) {
        val createPostRoute: String = Constants.POST_EDITOR_ROUTE
            .replace(Regex("/${Constants.NAV_ARG_REGEX}"),"")
    }

    companion object {
        fun replaceNavArgTemplate(route: String, navArg: String) = Regex(
            Constants.NAV_ARG_REGEX
        ).replace(route, navArg)
    }
    fun getStringRouteWithNavArg(navArg: String? = null): String = navArg?.let {
        replaceNavArgTemplate(route, navArg)
    } ?: route

    fun getRouteWithNavArg(navArg: String): Route {
        val cloneableRoute: Route = this.clone() as Route
        cloneableRoute.route = replaceNavArgTemplate(route, navArg)
        return cloneableRoute
    }
}

