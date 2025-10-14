package com.arfdevs.productmonitoring.domain.model

data class LoginModel(
    val username: String = "",
    val role: String = "",
    val token: String = "",
)
