package com.arfdevs.productmonitoring.di

import com.arfdevs.productmonitoring.BuildConfig
import com.arfdevs.productmonitoring.data.local.SessionManager
import com.arfdevs.productmonitoring.data.remote.ApiService
import com.arfdevs.productmonitoring.data.remote.AuthInterceptor
import com.arfdevs.productmonitoring.data.remote.NetworkResponseAdapterFactory
import com.chuckerteam.chucker.api.ChuckerInterceptor
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.runBlocking
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val networkModule = module {

    single {
        ChuckerInterceptor.Builder(androidContext()).build()
    }

    single {
        HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
    }

    //okhttp
    single {
        val sessionManager: SessionManager = get()

        val authInterceptor = AuthInterceptor {
            runBlocking {
                sessionManager.token.firstOrNull()
            }
        }

        OkHttpClient.Builder()
            .addInterceptor(authInterceptor)
            .addInterceptor(get<ChuckerInterceptor>())
            .addInterceptor(get<HttpLoggingInterceptor>())
            .build()
    }

    single {
        NetworkResponseAdapterFactory()
    }

    //retrofit
    single {
        Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .client(get<OkHttpClient>())
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(get<NetworkResponseAdapterFactory>())
            .build()
    }

    //service
    single {
        get<Retrofit>().create(ApiService::class.java)
    }
}
