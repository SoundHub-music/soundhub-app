package com.soundhub.data.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.soundhub.data.datastore.model.UserPreferences
import com.soundhub.utils.constants.Constants
import com.soundhub.utils.constants.Constants.DATASTORE_ACCESS_TOKEN
import com.soundhub.utils.constants.Constants.DATASTORE_REFRESH_TOKEN
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(
	name = Constants.DATASTORE_USER_CREDS
)

interface UserPreferencesObject {
	val accessToken: Preferences.Key<String>
	val refreshToken: Preferences.Key<String>
}

class UserCredsStore(private val context: Context) :
	BaseDataStore<UserPreferences, UserPreferencesObject>() {

	override val preferenceKeys = object : UserPreferencesObject {
		override val accessToken: Preferences.Key<String> =
			stringPreferencesKey(DATASTORE_ACCESS_TOKEN)

		override val refreshToken: Preferences.Key<String> =
			stringPreferencesKey(DATASTORE_REFRESH_TOKEN)
	}

	override suspend fun updateCreds(creds: UserPreferences?) {
		context.dataStore.edit { pref ->
			pref[preferenceKeys.accessToken] = creds?.accessToken ?: ""
			pref[preferenceKeys.refreshToken] = creds?.refreshToken ?: ""
		}
	}

	override fun getCreds(): Flow<UserPreferences> {
		return context.dataStore.data.map { pref ->
			UserPreferences(
				accessToken = pref[preferenceKeys.accessToken],
				refreshToken = pref[preferenceKeys.refreshToken]
			)
		}
	}

	override suspend fun clear() {
		context.dataStore.edit { it.clear() }
	}
}