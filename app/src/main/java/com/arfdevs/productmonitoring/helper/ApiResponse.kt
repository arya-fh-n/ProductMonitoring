package com.arfdevs.productmonitoring.helper

import com.google.gson.annotations.SerializedName

internal data class ApiResponse<T>(
    @field:SerializedName("status")
    val status: String? = null,

    @field:SerializedName("message")
    val message: String? = null,

    @field:SerializedName("data")
    val data: T? = null
)