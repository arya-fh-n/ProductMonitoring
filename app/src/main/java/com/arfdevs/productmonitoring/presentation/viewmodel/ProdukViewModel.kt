package com.arfdevs.productmonitoring.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.arfdevs.productmonitoring.domain.model.ProdukModel
import com.arfdevs.productmonitoring.domain.repository.ProdukRepository
import com.arfdevs.productmonitoring.helper.CoroutinesDispatcherProvider
import com.arfdevs.productmonitoring.helper.DomainResult
import com.arfdevs.productmonitoring.helper.UiState
import com.arfdevs.productmonitoring.helper.orZero
import kotlinx.coroutines.launch

class ProdukViewModel(
    private val repository: ProdukRepository,
    private val dispatcher: CoroutinesDispatcherProvider
) : ViewModel() {

    private val _listProduk = MutableLiveData<UiState<List<ProdukModel>>>()
    val listToko: LiveData<UiState<List<ProdukModel>>> = _listProduk

    fun getProduk() = viewModelScope.launch(dispatcher.io) {
        _listProduk.postValue(UiState.Loading)

        val uiState = when (val result = repository.getProduk()) {
            is DomainResult.EmptyState -> UiState.Empty
            is DomainResult.ErrorState -> UiState.Error(
                result.message.orEmpty(),
                result.responseStatusCode.orZero()
            )

            DomainResult.NetworkError -> UiState.ErrorConnection
            is DomainResult.TechnicalError -> UiState.Error("Technical Error", result.code.orZero())

            is DomainResult.Success -> UiState.Success(result.data)
        }

        _listProduk.postValue(uiState)
    }

}
