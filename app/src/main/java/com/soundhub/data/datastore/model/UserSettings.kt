package com.soundhub.data.datastore.model

import com.soundhub.utils.enums.AppTheme

data class UserSettings(
    val appTheme: AppTheme = AppTheme.AUTO
)
