package com.arfdevs.productmonitoring.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.arfdevs.productmonitoring.domain.model.ProdukTokoModel
import com.arfdevs.productmonitoring.domain.model.TokoModel
import com.arfdevs.productmonitoring.domain.repository.TokoRepository
import com.arfdevs.productmonitoring.helper.CoroutinesDispatcherProvider
import com.arfdevs.productmonitoring.helper.DomainResult
import com.arfdevs.productmonitoring.helper.UiState
import com.arfdevs.productmonitoring.helper.orZero
import kotlinx.coroutines.launch

class TokoViewModel(
    private val repository: TokoRepository,
    private val dispatcher: CoroutinesDispatcherProvider
) : ViewModel() {

    private val _listToko = MutableLiveData<UiState<List<TokoModel>>>()
    val listToko: LiveData<UiState<List<TokoModel>>> = _listToko

    private val _listProdukToko = MutableLiveData<UiState<List<ProdukTokoModel>>>()
    val listProdukToko: LiveData<UiState<List<ProdukTokoModel>>> = _listProdukToko

    fun getToko() = viewModelScope.launch(dispatcher.io) {
        _listToko.postValue(UiState.Loading)
        val uiState = when (val result = repository.getToko()) {
            is DomainResult.EmptyState -> UiState.Empty
            is DomainResult.ErrorState -> UiState.Error(
                result.message.orEmpty(),
                result.responseStatusCode.orZero()
            )

            DomainResult.NetworkError -> UiState.ErrorConnection
            is DomainResult.TechnicalError -> UiState.Error("Technical Error", result.code.orZero())
            is DomainResult.Success -> UiState.Success(result.data)
        }

        _listToko.postValue(uiState)
    }

    fun getProdukToko(idToko: Int) = viewModelScope.launch {
        _listProdukToko.postValue(UiState.Loading)
        val uiState = when (val result = repository.getProdukToko(idToko)) {
            is DomainResult.EmptyState -> UiState.Empty
            is DomainResult.ErrorState -> UiState.Error(
                result.message.orEmpty(),
                result.responseStatusCode.orZero()
            )

            DomainResult.NetworkError -> UiState.ErrorConnection
            is DomainResult.TechnicalError -> UiState.Error("Technical Error", result.code.orZero())
            is DomainResult.Success -> UiState.Success(result.data)
        }

        _listProdukToko.postValue(uiState)
    }
}