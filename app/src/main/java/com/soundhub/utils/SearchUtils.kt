package com.soundhub.utils

import com.soundhub.data.model.User

class SearchUtils {
    companion object {
        fun compareWithUsername(user: User?, searchBarText: String): Boolean {
            return user?.firstName?.lowercase()?.contains(searchBarText.lowercase()) == true ||
                    user?.lastName?.lowercase()?.contains(searchBarText.lowercase()) == true
        }
    }
}