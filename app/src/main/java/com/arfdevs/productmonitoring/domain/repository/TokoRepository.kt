package com.arfdevs.productmonitoring.domain.repository

import com.arfdevs.productmonitoring.domain.model.ProdukTokoModel
import com.arfdevs.productmonitoring.domain.model.TokoModel
import com.arfdevs.productmonitoring.helper.DomainResult

interface TokoRepository {

    suspend fun getToko(): DomainResult<List<TokoModel>>

    suspend fun getProdukToko(idToko: Int): DomainResult<List<ProdukTokoModel>>

}
