package com.arfdevs.productmonitoring.data.local.datastore

import android.content.Context
import androidx.datastore.preferences.preferencesDataStore

val Context.dataStore by preferencesDataStore(name = "auth_prefs")
