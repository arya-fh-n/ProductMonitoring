package com.arfdevs.productmonitoring.helper

sealed class NetworkResultWrapper<T> {
    data class Success<T>(
        val status: String,
        val code: Int,
        val message: String,
        val data: T,
        val headers: Map<String, List<String>>
    ) : NetworkResultWrapper<T>()

    data class Error<T>(
        val status: String,
        val code: Int,
        val message: String,
        val error: String,
        val headers: Map<String, List<String>>
    ) : NetworkResultWrapper<T>()

    data class Exception<T>(val throwable: Throwable) : NetworkResultWrapper<T>()
}
