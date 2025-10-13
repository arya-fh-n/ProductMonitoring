package com.arfdevs.productmonitoring.data.remote.request

import com.google.gson.annotations.SerializedName

data class ReportAttendanceRequest(
    @SerializedName("status")
    val status: String
)
