package com.arfdevs.productmonitoring.di

import androidx.room.Room
import com.arfdevs.productmonitoring.data.local.SessionManager
import com.arfdevs.productmonitoring.data.local.datastore.dataStore
import com.arfdevs.productmonitoring.data.local.db.Database
import com.arfdevs.productmonitoring.helper.Local
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val localModule = module {
    single { androidContext().dataStore }
    single { SessionManager(get()) }

    single {
        Room.databaseBuilder(androidContext(), Database::class.java, Local.DATABASE)
            .fallbackToDestructiveMigration(true)
            .build()
    }

    single {
        get<Database>().Dao()
    }
}
