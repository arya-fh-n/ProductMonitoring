package com.arfdevs.productmonitoring

import android.app.Application
import com.arfdevs.productmonitoring.di.localModule
import com.arfdevs.productmonitoring.di.networkModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class ProductMonitoringApp: Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger()
            androidContext(this@ProductMonitoringApp)

            modules(
                networkModule,
                localModule
            )
        }
    }

}