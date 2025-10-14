package com.arfdevs.productmonitoring.di

import com.arfdevs.productmonitoring.helper.CoroutinesDispatcherProvider
import org.koin.dsl.module

val dispatchersModule = module {
    single { CoroutinesDispatcherProvider() }
}