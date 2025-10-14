package com.arfdevs.productmonitoring.di

import com.arfdevs.productmonitoring.domain.mapper.Mapper
import com.arfdevs.productmonitoring.domain.mapper.MapperImpl
import org.koin.dsl.module

val mapperModule = module {
    single<Mapper> {
        MapperImpl(get())
    }
}