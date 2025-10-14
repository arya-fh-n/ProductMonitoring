package com.arfdevs.productmonitoring.domain.repository

import com.arfdevs.productmonitoring.domain.model.ProdukModel
import com.arfdevs.productmonitoring.helper.DomainResult

interface ProdukRepository {

    suspend fun getProduk(): DomainResult<List<ProdukModel>>

}
