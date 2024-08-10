package com.soundhub.utils.lib

import com.soundhub.data.model.User

class SearchUtils {
	companion object {
		fun compareWithUsername(user: User, searchBarText: String): Boolean {
			return user.firstName.lowercase().contains(searchBarText.lowercase()) ||
					user.lastName.lowercase().contains(searchBarText.lowercase())
		}
	}
}