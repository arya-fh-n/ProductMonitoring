package com.arfdevs.productmonitoring.data.repository

import com.arfdevs.productmonitoring.data.local.db.Dao
import com.arfdevs.productmonitoring.data.local.db.entity.ProdukEntity
import com.arfdevs.productmonitoring.data.remote.ApiService
import com.arfdevs.productmonitoring.domain.mapper.Mapper
import com.arfdevs.productmonitoring.domain.model.ProdukModel
import com.arfdevs.productmonitoring.domain.repository.ProdukRepository
import com.arfdevs.productmonitoring.helper.CoroutinesDispatcherProvider
import com.arfdevs.productmonitoring.helper.DomainResult
import com.arfdevs.productmonitoring.helper.orZero
import com.arfdevs.productmonitoring.helper.processResponse
import kotlinx.coroutines.withContext

class ProdukRepositoryImpl(
    private val apiService: ApiService,
    private val dao: Dao,
    private val mapper: Mapper,
    private val dispatcher: CoroutinesDispatcherProvider
) : ProdukRepository {

    override suspend fun getProduk(): DomainResult<List<ProdukModel>> = withContext(dispatcher.io) {
        val localData = dao.getAllProduk().map { mapper.mapProdukToDomain(it) }

        if (localData.isNotEmpty()) {
            return@withContext DomainResult.Success(localData)
        }

        val result = apiService.getProducts()

        return@withContext processResponse(result) { response ->
            response.products?.forEach { produk ->
                produk?.let {
                    dao.insertProduk(
                        ProdukEntity(
                            id = it.id.orZero(),
                            namaProduk = it.namaProduk.orEmpty(),
                            harga = it.harga.orZero(),
                            barcode = it.barcode.orEmpty()
                        )
                    )
                }
            }

            val newLocalData = dao.getAllProduk().map { mapper.mapProdukToDomain(it) }
            DomainResult.Success(newLocalData)
        }
    }

}
