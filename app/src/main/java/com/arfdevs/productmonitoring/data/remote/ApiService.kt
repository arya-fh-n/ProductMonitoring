package com.arfdevs.productmonitoring.data.remote

import com.arfdevs.productmonitoring.data.remote.request.LoginRequest
import com.arfdevs.productmonitoring.data.remote.request.RemovePromoProductRequest
import com.arfdevs.productmonitoring.data.remote.request.ReportAttendanceRequest
import com.arfdevs.productmonitoring.data.remote.request.ReportProductRequest
import com.arfdevs.productmonitoring.data.remote.request.ReportPromoRequest
import com.arfdevs.productmonitoring.data.remote.response.ListProdukResponse
import com.arfdevs.productmonitoring.data.remote.response.ListTokoResponse
import com.arfdevs.productmonitoring.data.remote.response.LoginResponse
import com.arfdevs.productmonitoring.helper.NetworkResultWrapper
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiService {
    @POST("login")
    suspend fun login(@Body request: LoginRequest): NetworkResultWrapper<LoginResponse>

    @POST("report/attendance")
    suspend fun reportAttendance(@Body request: ReportAttendanceRequest): NetworkResultWrapper<Unit>

    @POST("report/product")
    suspend fun addProductToStore(@Body request: ReportProductRequest): NetworkResultWrapper<Unit>

    @POST("report/promo")
    suspend fun addPromo(@Body request: ReportPromoRequest): NetworkResultWrapper<Unit>

    @POST("report/product/remove")
    suspend fun removeProductFromStore(@Body request: RemovePromoProductRequest): NetworkResultWrapper<Unit>

    @POST("report/promo/remove")
    suspend fun removePromoFromProduct(@Body request: RemovePromoProductRequest): NetworkResultWrapper<Unit>

    @GET("products")
    suspend fun getProducts(): NetworkResultWrapper<ListProdukResponse>

    @GET("stores")
    suspend fun getStores(): NetworkResultWrapper<ListTokoResponse>
}