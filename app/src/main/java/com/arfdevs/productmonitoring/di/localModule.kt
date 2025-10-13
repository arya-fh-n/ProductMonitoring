package com.arfdevs.productmonitoring.di

import com.arfdevs.productmonitoring.data.local.TokenManager
import com.arfdevs.productmonitoring.data.local.datastore.dataStore
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val localModule = module {
    single { androidContext().dataStore }
    single { TokenManager(get()) }
}
