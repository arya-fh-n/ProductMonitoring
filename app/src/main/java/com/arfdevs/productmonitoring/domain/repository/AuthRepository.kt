package com.arfdevs.productmonitoring.domain.repository

import com.arfdevs.productmonitoring.domain.model.LoginModel
import com.arfdevs.productmonitoring.helper.DomainResult

interface AuthRepository {

    suspend fun login(username: String, password: String): DomainResult<LoginModel>
    suspend fun getCurrentUser(): DomainResult<LoginModel>

}