package com.arfdevs.productmonitoring.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.arfdevs.productmonitoring.domain.model.LoginModel
import com.arfdevs.productmonitoring.domain.repository.AuthRepository
import com.arfdevs.productmonitoring.helper.CoroutinesDispatcherProvider
import com.arfdevs.productmonitoring.helper.DomainResult
import com.arfdevs.productmonitoring.helper.UiState
import com.arfdevs.productmonitoring.helper.orZero
import kotlinx.coroutines.launch

class AuthViewModel(
    private val repository: AuthRepository,
    private val dispatcher: CoroutinesDispatcherProvider
) : ViewModel() {

    private val _session = MutableLiveData<UiState<LoginModel>>()
    val session: LiveData<UiState<LoginModel>> = _session

    private val _logoutState = MutableLiveData<UiState<Boolean>>(UiState.Idle)
    val logoutState: LiveData<UiState<Boolean>> get() = _logoutState

    fun login(
        username: String,
        password: String
    ) = viewModelScope.launch(dispatcher.io) {
        _session.postValue(UiState.Loading)

        val uiState = when (val result = repository.login(username, password)) {
            is DomainResult.EmptyState -> UiState.Empty
            is DomainResult.ErrorState -> UiState.Error(
                result.message.orEmpty(),
                result.responseStatusCode.orZero()
            )

            DomainResult.NetworkError -> UiState.ErrorConnection
            is DomainResult.TechnicalError -> UiState.Error("Technical Error", result.code.orZero())
            is DomainResult.Success -> UiState.Success(result.data)
        }

        _session.postValue(uiState)
    }

    fun getCurrentUser() = viewModelScope.launch {
        _session.postValue(UiState.Loading)

        val uiState = when (val result = repository.getCurrentUser()) {
            is DomainResult.ErrorState -> UiState.Error(
                result.message.orEmpty(),
                result.responseStatusCode.orZero()
            )

            is DomainResult.Success -> UiState.Success(result.data)
            else -> {
                UiState.Error(
                    "An unexpected error occurred",
                    401
                )
            }
        }

        _session.postValue(uiState)
    }

    fun logOut() = viewModelScope.launch(dispatcher.io) {
        repository.logout()
        _logoutState.postValue(UiState.Success(true))
    }

}
