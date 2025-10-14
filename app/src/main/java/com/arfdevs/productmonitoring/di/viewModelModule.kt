package com.arfdevs.productmonitoring.di

import com.arfdevs.productmonitoring.presentation.viewmodel.AuthViewModel
import com.arfdevs.productmonitoring.presentation.viewmodel.ProdukViewModel
import com.arfdevs.productmonitoring.presentation.viewmodel.ReportViewModel
import com.arfdevs.productmonitoring.presentation.viewmodel.TokoViewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module


val viewModelModule = module {
    viewModelOf(::AuthViewModel)
    viewModelOf(::TokoViewModel)
    viewModelOf(::ProdukViewModel)
    viewModelOf(::ReportViewModel)
}