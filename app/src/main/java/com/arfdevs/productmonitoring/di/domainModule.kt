package com.arfdevs.productmonitoring.di

import com.arfdevs.productmonitoring.data.repository.AuthRepositoryImpl
import com.arfdevs.productmonitoring.data.repository.ProdukRepositoryImpl
import com.arfdevs.productmonitoring.data.repository.ReportRepositoryImpl
import com.arfdevs.productmonitoring.data.repository.TokoRepositoryImpl
import com.arfdevs.productmonitoring.domain.repository.AuthRepository
import com.arfdevs.productmonitoring.domain.repository.ProdukRepository
import com.arfdevs.productmonitoring.domain.repository.ReportRepository
import com.arfdevs.productmonitoring.domain.repository.TokoRepository
import org.koin.dsl.module

val domainModule = module {
    single<AuthRepository> {
        AuthRepositoryImpl(get(), get(), get())
    }

    single<TokoRepository> {
        TokoRepositoryImpl(get(), get(), get(), get())
    }

    single<ProdukRepository> {
        ProdukRepositoryImpl(get(), get(), get(), get())
    }

    single<ReportRepository> {
        ReportRepositoryImpl(get(), get(), get(), get())
    }
}
