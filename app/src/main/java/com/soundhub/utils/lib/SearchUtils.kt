package com.soundhub.utils.lib

import com.soundhub.data.model.User

class SearchUtils {
	companion object {
		fun compareWithUsername(user: User, searchBarText: String): Boolean {
			return user.firstName.lowercase().startsWith(searchBarText.lowercase())
					|| user.lastName.lowercase().startsWith(searchBarText.lowercase())
					|| user.getFullName().lowercase().startsWith(searchBarText.lowercase())
		}
	}
}