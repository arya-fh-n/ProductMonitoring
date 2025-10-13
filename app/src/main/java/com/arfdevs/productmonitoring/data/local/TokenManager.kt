package com.arfdevs.productmonitoring.data.local

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class TokenManager(private val dataStore: DataStore<Preferences>) {
    suspend fun saveToken(token: String) {
        dataStore.edit { prefs ->
            prefs[AUTH_TOKEN_KEY] = token
        }
    }

    val token: Flow<String?> = dataStore.data
        .map { prefs -> prefs[AUTH_TOKEN_KEY] }

    suspend fun clearToken() {
        dataStore.edit { prefs ->
            prefs.remove(AUTH_TOKEN_KEY)
        }
    }

    companion object {
        private val AUTH_TOKEN_KEY = stringPreferencesKey("auth_token")
    }
}