package com.soundhub.data.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.soundhub.data.model.User
import com.soundhub.utils.Constants
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import java.util.UUID

data class UserPreferences(
    val id: UUID? = null,
    val firstName: String? = null,
    val lastName: String? = null,
    val country: String? = null,
    val city: String? = null,
    val email: String? = null,
    val description: String? = null,
    val token: String? = null,
)

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(
    name = Constants.DATASTORE_USER_CREDS
)

class UserStore(private val context: Context) {
     private object PreferenceKeys: Iterable<Preferences.Key<String>> {
         val email: Preferences.Key<String> = stringPreferencesKey(Constants.DATASTORE_USER_EMAIL)
         val firstName: Preferences.Key<String> = stringPreferencesKey(Constants.DATASTORE_USER_FIRST_NAME)
         val lastName: Preferences.Key<String> = stringPreferencesKey(Constants.DATASTORE_USER_LASTNAME)
         val country: Preferences.Key<String> = stringPreferencesKey(Constants.DATASTORE_USER_COUNTRY)
         val city: Preferences.Key<String> = stringPreferencesKey(Constants.DATASTORE_USER_CITY)
         val userId: Preferences.Key<String> = stringPreferencesKey(Constants.DATASTORE_USER_ID)
         val description: Preferences.Key<String> = stringPreferencesKey(Constants.DATASTORE_USER_DESCRIPTION)
         val token: Preferences.Key<String> = stringPreferencesKey(Constants.DATASTORE_SESSION_TOKEN)

         override fun iterator(): Iterator<Preferences.Key<String>> {
             return listOf(
                 email, firstName,
                 lastName, country,
                 city, userId,
                 description, token
             ).iterator()
         }
     }

    suspend fun saveUser(user: User) {
        context.dataStore.edit {pref ->
            pref[PreferenceKeys.email] = user.email ?: ""
            pref[PreferenceKeys.firstName] = user.firstName ?: ""
            pref[PreferenceKeys.lastName] = user.lastName ?: ""
            pref[PreferenceKeys.userId] = user.id.toString()
            pref[PreferenceKeys.country] = user.country ?: ""
            pref[PreferenceKeys.city] = user.city ?: ""
            pref[PreferenceKeys.token] = user.token ?: ""
            pref[PreferenceKeys.description] = user.description ?: ""
        }
    }

    suspend fun <T> updateField(key: Preferences.Key<T>, value: T) {
        context.dataStore.edit { pref -> pref[key] = value }
    }

    fun getCreds(): Flow<UserPreferences> {
        return context.dataStore.data.map {
            pref ->
            UserPreferences(
                id = runCatching { UUID.fromString(pref[PreferenceKeys.userId]) }
                    .getOrElse { null },
                firstName = pref[PreferenceKeys.firstName],
                lastName = pref[PreferenceKeys.lastName],
                city = pref[PreferenceKeys.city],
                country = pref[PreferenceKeys.country],
                email = pref[PreferenceKeys.email]
            )
        }
    }

    fun getUser(): Flow<User> {
        return context.dataStore.data.map {
            pref ->
            User(
                id = runCatching { UUID.fromString(pref[PreferenceKeys.userId]) }
                    .getOrElse { UUID.randomUUID() },
                firstName = pref[PreferenceKeys.firstName],
                lastName = pref[PreferenceKeys.lastName],
                city = pref[PreferenceKeys.city],
                country = pref[PreferenceKeys.country],
                email = pref[PreferenceKeys.email]
            )
        }
    }

    fun getData(key: Preferences.Key<String>): Flow<String?> {
        return context.dataStore.data.map { pref -> pref[key] }
    }

    suspend fun ifUserStored(): Boolean {
        return PreferenceKeys.all { key -> context.dataStore.data.map { it[key] }.first() != null }
    }

    suspend fun clear() = context.dataStore.edit { it.clear() }
}