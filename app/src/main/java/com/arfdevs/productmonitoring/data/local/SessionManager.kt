package com.arfdevs.productmonitoring.data.local

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.arfdevs.productmonitoring.helper.Local
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class SessionManager(private val dataStore: DataStore<Preferences>) {
    suspend fun saveSession(username: String, token: String) {
        dataStore.edit { prefs ->
            prefs[USERNAME] = username
            prefs[AUTH_TOKEN_KEY] = token
        }
    }

    val token: Flow<String?> = dataStore.data.map { prefs ->
        prefs[AUTH_TOKEN_KEY]
    }

    val username: Flow<String> = dataStore.data.map {  prefs ->
        prefs[USERNAME] ?: ""
    }

    suspend fun clearToken() {
        dataStore.edit { prefs ->
            prefs.remove(AUTH_TOKEN_KEY)
            prefs.remove(USERNAME)
        }
    }

    companion object {
        private val AUTH_TOKEN_KEY = stringPreferencesKey(Local.AUTH_TOKEN)
        private val USERNAME = stringPreferencesKey(Local.USERNAME)
    }
}