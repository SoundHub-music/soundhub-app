package com.soundhub

import com.soundhub.utils.constants.Constants

sealed class Route(var route: String) : Cloneable {
	data object Authentication : Route(route = Constants.AUTHENTICATION_ROUTE) {
		val withNavArg = "$route/{${Constants.POST_REGISTER_NAV_ARG}}"

		data object ChooseGenres : Route(route = Constants.CHOOSE_GENRES_ROUTE)
		data object ChooseArtists : Route(route = Constants.CHOOSE_ARTISTS_ROUTE)
		data object FillUserData : Route(route = Constants.FILL_DATA_REGISTRATION_ROUTE)
	}

	data object Profile : Route(route = Constants.PROFILE_ROUTE) {
		data object Friends : Route(route = Constants.FRIENDS_ROUTE)
	}

	data object PostLine : Route(route = Constants.POSTLINE_ROUTE)
	data object Music : Route(route = Constants.MUSIC_ROUTE) {
		data object NewOfTheWeek : Route(route = Constants.MUSIC_NEW_OF_THE_WEEK)
		data object NewOfTheMonth : Route(route = Constants.MUSIC_NEW_OF_THE_MONTH)
		data object RecommendMusic : Route(route = Constants.MUSIC_RECOMMENDATIONS)
		data object LastFmProfile: Route(route = Constants.LASTFM_PROFILE)

		data object FavoriteGenres : Route(route = Constants.MUSIC_FAVORITE_GENRES)
		data object FavoriteArtists : Route(route = Constants.MUSIC_FAVORITE_ARTISTS)
		data object Playlists : Route(route = Constants.MUSIC_PLAYLISTS)
	}


	data object Gallery : Route(route = Constants.GALLERY_ROUTE)

	data object Messenger : Route(route = Constants.MESSENGER_ROUTE) {
		data object Chat : Route(route = Constants.MESSENGER_CHAT_ROUTE)
	}

	data object Settings : Route(route = Constants.SETTINGS_ROUTE)
	data object Notifications : Route(route = Constants.NOTIFICATIONS_ROUTE)
	data object EditUserData : Route(route = Constants.EDIT_USER_DATA_ROUTE)
	data object EditFavoriteArtists : Route(route = Constants.EDIT_FAV_ARTISTS_ROUTE)
	data object EditFavoriteGenres : Route(route = Constants.EDIT_FAV_GENRES_ROUTE)
	data object PostEditor : Route(route = Constants.POST_EDITOR_ROUTE) {
		val createPostRoute: String = Constants.POST_EDITOR_ROUTE
			.replace(Regex("/${Constants.DYNAMIC_PARAM_REGEX}"), "")
	}

	companion object {
		fun replaceNavArgTemplate(route: String, navArg: String) = Regex(
			Constants.DYNAMIC_PARAM_REGEX
		).replaceFirst(route, navArg)

		fun replaceNavArgsTemplate(route: String, vararg navArgs: String): String {
			var result: String = route
			navArgs.forEach { arg -> result = replaceNavArgTemplate(result, arg) }
			return result
		}
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

