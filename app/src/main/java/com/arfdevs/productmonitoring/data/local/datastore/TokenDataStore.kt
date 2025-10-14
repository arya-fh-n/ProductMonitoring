package com.arfdevs.productmonitoring.data.local.datastore

import android.content.Context
import androidx.datastore.preferences.preferencesDataStore
import com.arfdevs.productmonitoring.helper.Local

val Context.dataStore by preferencesDataStore(name = Local.DATA_STORE)
