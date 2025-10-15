package com.arfdevs.productmonitoring.data.repository

import android.util.Log
import com.arfdevs.productmonitoring.data.local.SessionManager
import com.arfdevs.productmonitoring.data.remote.ApiService
import com.arfdevs.productmonitoring.data.remote.request.LoginRequest
import com.arfdevs.productmonitoring.domain.model.LoginModel
import com.arfdevs.productmonitoring.domain.repository.AuthRepository
import com.arfdevs.productmonitoring.helper.Constants
import com.arfdevs.productmonitoring.helper.CoroutinesDispatcherProvider
import com.arfdevs.productmonitoring.helper.DomainResult
import com.arfdevs.productmonitoring.helper.processResponse
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.withContext

class AuthRepositoryImpl(
    private val apiService: ApiService,
    private val sessionManager: SessionManager,
    private val dispatcher: CoroutinesDispatcherProvider
) : AuthRepository {

    override suspend fun login(
        username: String,
        password: String
    ): DomainResult<LoginModel> = withContext(dispatcher.io) {
        val result = apiService.login(
            LoginRequest(
                username = username,
                password = password
            )
        )

        return@withContext processResponse(result) { response ->
            Log.d("ARYUL", "login: $response")
            sessionManager.startNewSession(response)

            val loggedInUsername = response.user?.username.orEmpty()
            val role = response.user?.role.orEmpty()
            val sessionToken = response.token.orEmpty()

            DomainResult.Success(
                LoginModel(
                    username = loggedInUsername,
                    role = role,
                    token = sessionToken
                )
            )
        }
    }

    override suspend fun getCurrentUser(): DomainResult<LoginModel> = withContext(dispatcher.io) {
        val token = sessionManager.token.firstOrNull()
        val username = sessionManager.username.firstOrNull()

        return@withContext if (token.isNullOrBlank() || username.isNullOrBlank()) {
            DomainResult.ErrorState(
                message = "Session not found",
                responseStatusCode = Constants.SILENT_NAV_CODE
            )
        } else {
            DomainResult.Success(
                LoginModel(
                    username = username,
                    token = token
                )
            )
        }
    }

}
