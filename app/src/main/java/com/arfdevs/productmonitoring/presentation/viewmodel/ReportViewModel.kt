package com.arfdevs.productmonitoring.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.arfdevs.productmonitoring.domain.repository.ReportRepository
import com.arfdevs.productmonitoring.helper.CoroutinesDispatcherProvider
import com.arfdevs.productmonitoring.helper.DomainResult
import com.arfdevs.productmonitoring.helper.UiState
import com.arfdevs.productmonitoring.helper.orZero
import kotlinx.coroutines.launch

class ReportViewModel(
    private val repository: ReportRepository,
    private val dispatcher: CoroutinesDispatcherProvider
) : ViewModel() {

    private val _attendance = MutableLiveData<UiState<Boolean>>()
    val attendance: LiveData<UiState<Boolean>> = _attendance

    private val _reportProduct = MutableLiveData<UiState<Unit>>()
    val reportProduct: LiveData<UiState<Unit>> = _reportProduct

    private val _reportPromo = MutableLiveData<UiState<Unit>>()
    val reportPromo: LiveData<UiState<Unit>> = _reportPromo

    private val _removePromo = MutableLiveData<UiState<Unit>>()
    val removePromo: LiveData<UiState<Unit>> = _removePromo

    private val _removeProductFromStore = MutableLiveData<UiState<Unit>>()
    val removeProductFromStore: LiveData<UiState<Unit>> = _removeProductFromStore

    fun getLatestAttendance() = viewModelScope.launch(dispatcher.io) {
        _attendance.postValue(UiState.Loading)

        val uiState = when (val result = repository.getAttendance()) {
            is DomainResult.EmptyState -> UiState.Empty
            is DomainResult.ErrorState -> UiState.Error(
                result.message.orEmpty(),
                result.responseStatusCode.orZero()
            )

            DomainResult.NetworkError -> UiState.ErrorConnection
            is DomainResult.TechnicalError -> UiState.Error("Technical Error", result.code.orZero())
            is DomainResult.Success -> UiState.Success(result.data)
        }

        _attendance.postValue(uiState)
    }

    fun setAttendance(status: String) = viewModelScope.launch(dispatcher.io) {
        _attendance.postValue(UiState.Loading)

        val uiState = when (val result = repository.reportAttendance(status)) {
            is DomainResult.EmptyState -> UiState.Empty
            is DomainResult.ErrorState -> UiState.Error(
                result.message.orEmpty(),
                result.responseStatusCode.orZero()
            )

            DomainResult.NetworkError -> UiState.ErrorConnection
            is DomainResult.TechnicalError -> UiState.Error("Technical Error", result.code.orZero())
            is DomainResult.Success -> UiState.Success(result.data)
        }

        _attendance.postValue(uiState)
    }

    fun reportProduct(idToko: Int, idProduk: Int) = viewModelScope.launch(dispatcher.io) {
        _reportProduct.postValue(UiState.Loading)

        val uiState = when (val result = repository.reportProduct(idToko, idProduk)) {
            is DomainResult.EmptyState -> UiState.Empty
            is DomainResult.ErrorState -> UiState.Error(
                result.message.orEmpty(),
                result.responseStatusCode.orZero()
            )

            DomainResult.NetworkError -> UiState.ErrorConnection
            is DomainResult.TechnicalError -> UiState.Error("Technical Error", result.code.orZero())
            is DomainResult.Success -> UiState.Success(result.data)
        }

        _reportProduct.postValue(uiState)
    }

    fun clearReportProductState() {
        _reportProduct.postValue(UiState.Idle)
    }

    fun clearRemoveProductState() {
        _removeProductFromStore.postValue(UiState.Idle)
    }

    fun reportPromo(idToko: Int, idProduk: Int, hargaPromo: Int) =
        viewModelScope.launch(dispatcher.io) {
            _reportPromo.postValue(UiState.Loading)

            val uiState = when (val result = repository.reportPromo(idToko, idProduk, hargaPromo)) {
                is DomainResult.EmptyState -> UiState.Empty
                is DomainResult.ErrorState -> UiState.Error(
                    result.message.orEmpty(),
                    result.responseStatusCode.orZero()
                )

                DomainResult.NetworkError -> UiState.ErrorConnection
                is DomainResult.TechnicalError -> UiState.Error(
                    "Technical Error",
                    result.code.orZero()
                )

                is DomainResult.Success -> UiState.Success(result.data)
            }

            _reportPromo.postValue(uiState)
        }

    fun removePromo(idToko: Int, idProduk: Int) = viewModelScope.launch(dispatcher.io) {
        _removePromo.postValue(UiState.Loading)

        val uiState = when (val result = repository.removePromo(idToko, idProduk)) {
            is DomainResult.EmptyState -> UiState.Empty
            is DomainResult.ErrorState -> UiState.Error(
                result.message.orEmpty(),
                result.responseStatusCode.orZero()
            )

            DomainResult.NetworkError -> UiState.ErrorConnection
            is DomainResult.TechnicalError -> UiState.Error(
                "Technical Error",
                result.code.orZero()
            )

            is DomainResult.Success -> UiState.Success(result.data)
        }

        _removePromo.postValue(uiState)
    }

    fun removeProductFromStore(idToko: Int, idProduk: Int) = viewModelScope.launch(dispatcher.io) {
        _removeProductFromStore.postValue(UiState.Loading)

        val uiState = when (val result = repository.removeProductFromStore(idToko, idProduk)) {
            is DomainResult.EmptyState -> UiState.Empty
            is DomainResult.ErrorState -> UiState.Error(
                result.message.orEmpty(),
                result.responseStatusCode.orZero()
            )

            DomainResult.NetworkError -> UiState.ErrorConnection
            is DomainResult.TechnicalError -> UiState.Error(
                "Technical Error",
                result.code.orZero()
            )

            is DomainResult.Success -> UiState.Success(result.data)
        }

        _removeProductFromStore.postValue(uiState)
    }

}
