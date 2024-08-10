package com.soundhub.data.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.soundhub.data.datastore.model.UserSettings
import com.soundhub.utils.constants.Constants.DATASTORE_APP_THEME
import com.soundhub.utils.constants.Constants.DATASTORE_USER_SETTINGS
import com.soundhub.utils.enums.AppTheme
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.settingsStore: DataStore<Preferences> by preferencesDataStore(
	name = DATASTORE_USER_SETTINGS
)

class UserSettingsStore(private val context: Context) : BaseDataStore<UserSettings>() {
	private object PreferenceKeys {
		val appTheme: Preferences.Key<String> = stringPreferencesKey(DATASTORE_APP_THEME)
	}

	override suspend fun updateCreds(creds: UserSettings?) {
		context.settingsStore.edit { pref ->
			pref[PreferenceKeys.appTheme] = creds?.appTheme?.toString() ?: ""
		}
	}

	override fun getCreds(): Flow<UserSettings> {
		return context.settingsStore.data.map { pref ->
			runCatching {
				UserSettings(
					appTheme = AppTheme.valueOf(pref[PreferenceKeys.appTheme].toString())
				)
			}.getOrDefault(UserSettings())
		}
	}

	override suspend fun clear() {
		context.settingsStore.edit { it.clear() }
	}
}