package com.arfdevs.productmonitoring.data.repository

import com.arfdevs.productmonitoring.data.local.db.Dao
import com.arfdevs.productmonitoring.data.local.db.entity.ProdukTokoEntity
import com.arfdevs.productmonitoring.data.local.db.entity.TokoEntity
import com.arfdevs.productmonitoring.data.remote.ApiService
import com.arfdevs.productmonitoring.domain.mapper.Mapper
import com.arfdevs.productmonitoring.domain.model.ProdukTokoModel
import com.arfdevs.productmonitoring.domain.model.TokoModel
import com.arfdevs.productmonitoring.domain.repository.TokoRepository
import com.arfdevs.productmonitoring.helper.CoroutinesDispatcherProvider
import com.arfdevs.productmonitoring.helper.DomainResult
import com.arfdevs.productmonitoring.helper.orZero
import com.arfdevs.productmonitoring.helper.processResponse
import kotlinx.coroutines.withContext

class TokoRepositoryImpl(
    private val apiService: ApiService,
    private val dao: Dao,
    private val mapper: Mapper,
    private val dispatcher: CoroutinesDispatcherProvider
) : TokoRepository {
    override suspend fun getToko(): DomainResult<List<TokoModel>> = withContext(dispatcher.io) {
        val localData = dao.getAllToko().map { mapper.mapTokoToDomain(it) }

        if (localData.isNotEmpty()) {
            return@withContext DomainResult.Success(localData)
        }

        val result = apiService.getStores()

        return@withContext processResponse(result) { response ->
            response.stores?.forEach { toko ->
                toko?.let {
                    dao.insertToko(
                        TokoEntity(
                            id = it.id.orZero(),
                            namaToko = it.namaToko.orEmpty(),
                            kodeToko = it.kodeToko.orEmpty(),
                            alamat = it.alamat.orEmpty()
                        )
                    )
                }
            }

            val newLocalData = dao.getAllToko().map { mapper.mapTokoToDomain(it) }
            DomainResult.Success(newLocalData)
        }
    }

    override suspend fun getProdukToko(
        idToko: Int
    ): DomainResult<List<ProdukTokoModel>> = withContext(dispatcher.io) {
        val localData = dao.getAllProdukTokoById(idToko).map {
            mapper.mapProdukTokoToDomain(it)
        }

        if (localData.isNotEmpty()) {
            return@withContext DomainResult.Success(localData)
        }

        val result = apiService.getStoreProducts(storeId = idToko)

        return@withContext processResponse(result) { response ->
            response.products?.forEach { produk ->
                produk?.let {
                    dao.insertProdukToko(
                        ProdukTokoEntity(
                            idToko = idToko,
                            idProduk = it.id.orZero(),
                            namaProduk = it.namaProduk.orEmpty(),
                            harga = it.harga.orZero(),
                            hargaPromo = it.hargaPromo.orZero(),
                            barcode = it.barcode.orEmpty()
                        )
                    )
                }
            }

            val newLocalData = dao.getAllProdukTokoById(idToko).map {
                mapper.mapProdukTokoToDomain(it)
            }
            DomainResult.Success(newLocalData)
        }
    }
}