package com.arfdevs.productmonitoring.data.remote

import android.util.Log
import com.arfdevs.productmonitoring.helper.ApiResponse
import com.arfdevs.productmonitoring.helper.NetworkResultWrapper
import com.google.gson.Gson
import com.google.gson.JsonObject
import okhttp3.Request
import okio.Timeout
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

internal class NetworkResponseCall<T>(
    private val delegate: Call<ApiResponse<T>>
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
        delegate.enqueue(object : Callback<ApiResponse<T>> {
            override fun onResponse(
                call: Call<ApiResponse<T>>,
                response: Response<ApiResponse<T>>
            ) {
                val headers = response.headers().toMultimap()
                val code = response.code()
                val apiResponse = response.body()

                Log.d("ARYUL", "onResponse: ${response.body()}")
                Log.d("ARYUL", "onResponse Success: ${response.isSuccessful}")
                Log.d("ARYUL", "Raw response body: $apiResponse")
                Log.d("ARYUL", "Status: ${apiResponse?.status.orEmpty()}")
                Log.d("ARYUL", "Message: ${apiResponse?.message.orEmpty()}")
                Log.d("ARYUL", "Data: ${apiResponse?.data}")

                if (response.isSuccessful && response.body() != null) {
                    callback.onResponse(
                        this@NetworkResponseCall,
                        Response.success(
                            NetworkResultWrapper.Success(
                                status = apiResponse?.status.orEmpty(),
                                code = code,
                                message = apiResponse?.message.orEmpty(),
                                data = apiResponse?.data,
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
            }


            override fun onFailure(call: Call<ApiResponse<T>>, t: Throwable) {
                Log.d("ARYA", "onFailure: Failure: $t")
                callback.onResponse(
                    this@NetworkResponseCall,
                    Response.success(NetworkResultWrapper.Exception(t))
                )
                call.cancel()
            }
        })
    }
}
