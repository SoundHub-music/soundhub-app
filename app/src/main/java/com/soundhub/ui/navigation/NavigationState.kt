package com.soundhub.ui.navigation

import com.soundhub.utils.Routes

data class NavigationState(
    val screenTitle: String? = "",
    val currentRoute: Routes = Routes.AUTHENTICATION
)
