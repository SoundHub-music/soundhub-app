package com.soundhub.data.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.soundhub.utils.constants.Constants
import com.soundhub.utils.constants.Constants.DATASTORE_ACCESS_TOKEN
import com.soundhub.utils.constants.Constants.DATASTORE_REFRESH_TOKEN
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map


private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(
    name = Constants.DATASTORE_USER_CREDS
)

class UserCredsStore(private val context: Context) {
     private object PreferenceKeys {
         val accessToken: Preferences.Key<String> = stringPreferencesKey(DATASTORE_ACCESS_TOKEN)
         val refreshToken: Preferences.Key<String> = stringPreferencesKey(DATASTORE_REFRESH_TOKEN)
     }

    suspend fun updateCreds(creds: UserPreferences?) {
        context.dataStore.edit { pref ->
            pref[PreferenceKeys.accessToken] = creds?.accessToken ?: ""
            pref[PreferenceKeys.refreshToken] = creds?.refreshToken ?: ""
        }
    }

    fun getCreds(): Flow<UserPreferences> {
        return context.dataStore.data.map {
            pref -> UserPreferences(
                accessToken = pref[PreferenceKeys.accessToken],
                refreshToken = pref[PreferenceKeys.refreshToken]
            )
        }
    }

    suspend fun clear() = context.dataStore.edit { it.clear() }
}