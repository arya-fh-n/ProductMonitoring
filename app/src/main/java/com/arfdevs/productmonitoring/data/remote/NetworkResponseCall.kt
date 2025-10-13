package com.arfdevs.productmonitoring.data.remote

import com.arfdevs.productmonitoring.helper.NetworkResultWrapper
import com.google.gson.Gson
import com.google.gson.JsonObject
import okhttp3.Request
import okio.Timeout
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

internal class NetworkResponseCall<T>(
    private val delegate: Call<T>
) : Call<NetworkResultWrapper<T?>> {

    private val gson = Gson()

    override fun clone(): Call<NetworkResultWrapper<T?>> = NetworkResponseCall(delegate.clone())

    override fun execute(): Response<NetworkResultWrapper<T?>> =
        throw UnsupportedOperationException("Synchronous execution is not supported")

    override fun isExecuted(): Boolean = delegate.isExecuted

    override fun cancel() = delegate.cancel()

    override fun isCanceled(): Boolean = delegate.isCanceled

    override fun request(): Request = delegate.request()

    override fun timeout(): Timeout = delegate.timeout()

    override fun enqueue(callback: Callback<NetworkResultWrapper<T?>>) {
        delegate.enqueue(object : Callback<T> {
            override fun onResponse(call: Call<T>, response: Response<T>) {
                val headers = response.headers().toMultimap()
                val code = response.code()

                try {
                    if (response.isSuccessful && response.body() != null) {
                        // Try to cast body as ApiResponse if that’s your backend wrapper
                        val apiResponseJson = gson.toJsonTree(response.body()).asJsonObject

                        val status = apiResponseJson["status"]?.asString ?: "Success"
                        val message = apiResponseJson["message"]?.asString ?: ""
                        val data = gson.fromJson(apiResponseJson["data"], Any::class.java)

                        @Suppress("UNCHECKED_CAST")
                        callback.onResponse(
                            this@NetworkResponseCall,
                            Response.success(
                                NetworkResultWrapper.Success(
                                    status = status,
                                    code = code,
                                    message = message,
                                    data = data as T,
                                    headers = headers
                                )
                            )
                        )
                    } else {
                        val errorBody = response.errorBody()?.string()
                        val errorJson = try {
                            gson.fromJson(errorBody, JsonObject::class.java)
                        } catch (e: Exception) {
                            JsonObject()
                        }

                        val status = errorJson["status"]?.asString ?: "Error"
                        val message = errorJson["message"]?.asString ?: "Unknown error"
                        val error = errorJson["error"]?.asString ?: errorBody.orEmpty()

                        callback.onResponse(
                            this@NetworkResponseCall,
                            Response.success(
                                NetworkResultWrapper.Error(
                                    status = status,
                                    code = code,
                                    message = message,
                                    error = error,
                                    headers = headers
                                )
                            )
                        )
                    }
                } catch (e: Exception) {
                    callback.onResponse(
                        this@NetworkResponseCall,
                        Response.success(NetworkResultWrapper.Exception(e))
                    )
                }
            }

            override fun onFailure(call: Call<T>, t: Throwable) {
                callback.onResponse(
                    this@NetworkResponseCall,
                    Response.success(NetworkResultWrapper.Exception(t))
                )
                call.cancel()
            }
        })
    }
}
