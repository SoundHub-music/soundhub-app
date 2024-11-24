package com.soundhub.utils.lib

import com.soundhub.data.model.User

class SearchUtils {
	companion object {
		fun compareWithUsername(user: User, searchBarText: String): Boolean {
			val substring: String = searchBarText.lowercase()

			val fullName: String = user.getFullName().lowercase()
			val firstName: String = user.firstName.lowercase()
			val lastName: String = user.lastName.lowercase()

			return firstName.startsWith(substring) ||
					lastName.startsWith(substring) ||
					fullName.startsWith(substring)
		}
	}
}