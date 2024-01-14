package com.soundhub.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.forEach
import kotlinx.coroutines.flow.map

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_creds")
class UserStore(private val context: Context) {
     private object PreferenceKeys: Iterable<Preferences.Key<String>> {
         val email: Preferences.Key<String> = stringPreferencesKey("user_email")
         val name: Preferences.Key<String> = stringPreferencesKey("user_name")

         override fun iterator(): Iterator<Preferences.Key<String>> {
             return listOf(email, name).iterator()
         }
     }

    suspend fun saveUser(email: String, name: String) {
        context.dataStore.edit {pref ->
            pref[PreferenceKeys.email] = email
            pref[PreferenceKeys.name] = name
        }
    }

    suspend fun updateEmail(email: String) {
        context.dataStore.edit { pref -> pref[PreferenceKeys.email] = email }
    }

    suspend fun updateName(name: String) {
        context.dataStore.edit { pref -> pref[PreferenceKeys.name] = name }
    }

    fun getCreds(): Flow<Preferences> {
        return context.dataStore.data
    }

    fun getData(key: Preferences.Key<String>): Flow<String?> {
        return context.dataStore.data.map { pref -> pref[key] }
    }

    suspend fun ifUserStored(): Boolean {
        return PreferenceKeys.all { key -> context.dataStore.data.map { it[key] }.first() != null }
    }

    suspend fun clear() = context.dataStore.edit { it.clear() }
}