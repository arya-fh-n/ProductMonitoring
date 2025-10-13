package com.arfdevs.productmonitoring.data.remote

import com.arfdevs.productmonitoring.helper.NetworkResultWrapper
import retrofit2.Call
import retrofit2.CallAdapter
import java.lang.reflect.Type

internal class NetworkResponseCallAdapter<T>(
    private val successType: Type
) : CallAdapter<T, Call<NetworkResultWrapper<T?>>> {

    override fun responseType(): Type = successType

    override fun adapt(call: Call<T>): Call<NetworkResultWrapper<T?>> {
        return NetworkResponseCall(call)
    }
}
