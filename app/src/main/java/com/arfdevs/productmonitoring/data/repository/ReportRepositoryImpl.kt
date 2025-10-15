package com.arfdevs.productmonitoring.data.repository

import com.arfdevs.productmonitoring.data.local.SessionManager
import com.arfdevs.productmonitoring.data.local.db.Dao
import com.arfdevs.productmonitoring.data.local.db.entity.AttendanceEntity
import com.arfdevs.productmonitoring.data.local.db.entity.ProdukTokoEntity
import com.arfdevs.productmonitoring.data.remote.ApiService
import com.arfdevs.productmonitoring.data.remote.request.RemovePromoProductRequest
import com.arfdevs.productmonitoring.data.remote.request.ReportAttendanceRequest
import com.arfdevs.productmonitoring.data.remote.request.ReportProductRequest
import com.arfdevs.productmonitoring.data.remote.request.ReportPromoRequest
import com.arfdevs.productmonitoring.domain.repository.ReportRepository
import com.arfdevs.productmonitoring.helper.Constants
import com.arfdevs.productmonitoring.helper.CoroutinesDispatcherProvider
import com.arfdevs.productmonitoring.helper.DomainResult
import com.arfdevs.productmonitoring.helper.processResponse
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.withContext

class ReportRepositoryImpl(
    private val apiService: ApiService,
    private val dao: Dao,
    private val sessionManager: SessionManager,
    private val dispatcher: CoroutinesDispatcherProvider
) : ReportRepository {

    override suspend fun reportAttendance(
        status: String
    ): DomainResult<Boolean> = withContext(dispatcher.io) {
        val request = ReportAttendanceRequest(status = status)
        val result = apiService.reportAttendance(request)
        val username = sessionManager.username.firstOrNull()

        val localData = AttendanceEntity(
            status = status,
            username = username.orEmpty()
        )

        dao.insertAttendance(localData)

        return@withContext processResponse(result) {
            DomainResult.Success(status.equals(Constants.HADIR, ignoreCase = true))
        }
    }

    override suspend fun getAttendance(): DomainResult<Boolean> = withContext(dispatcher.io) {
        val localData = dao.getRecentAttendance()
        return@withContext if (localData.isEmpty()) {
            DomainResult.EmptyState("Tidak ada absensi", 401, 401)
        } else {
            val attendance = localData.getOrNull(0)
            DomainResult.Success(attendance?.status.equals(Constants.HADIR, ignoreCase = true))
        }
    }

    override suspend fun reportProduct(
        idToko: Int,
        idProduk: Int
    ): DomainResult<Unit> = withContext(dispatcher.io) {
        val request = ReportProductRequest(
            idToko = idToko,
            idProduk = idProduk
        )

        val result = apiService.addProductToStore(request)

        return@withContext processResponse(result) {
            val produk = dao.getAllProduk().find { it.id == idProduk }
            produk?.let {
                dao.insertProdukToko(
                    ProdukTokoEntity(
                        idToko = idToko,
                        idProduk = idProduk,
                        namaProduk = it.namaProduk,
                        harga = it.harga,
                        hargaPromo = 0,
                        barcode = it.barcode
                    )
                )
            }
            DomainResult.Success(Unit)
        }.also { domainResult ->
            if (domainResult !is DomainResult.Success) {
                val produk = dao.getAllProduk().find { it.id == idProduk }
                produk?.let {
                    dao.insertProdukToko(
                        ProdukTokoEntity(
                            idToko = idToko,
                            idProduk = idProduk,
                            namaProduk = it.namaProduk,
                            harga = it.harga,
                            hargaPromo = 0,
                            barcode = it.barcode,
                            isDirty = true
                        )
                    )
                }
            }
        }
    }

    override suspend fun reportPromo(
        idToko: Int,
        idProduk: Int,
        hargaPromo: Int
    ): DomainResult<Unit> = withContext(dispatcher.io) {
        val request = ReportPromoRequest(
            idToko = idToko,
            idProduk = idProduk,
            hargaPromo = hargaPromo
        )

        val result = apiService.addPromo(request)

        return@withContext processResponse(result) {
            dao.updateProdukTokoById(
                idToko = idToko,
                idProduk = idProduk,
                hargaPromo = hargaPromo
            )
            DomainResult.Success(Unit)
        }.also { domainResult ->
            if (domainResult !is DomainResult.Success) {
                dao.updateProdukTokoById(
                    idToko = idToko,
                    idProduk = idProduk,
                    hargaPromo = hargaPromo,
                    isDirty = true
                )
            }
        }
    }

    override suspend fun removePromo(
        idToko: Int,
        idProduk: Int
    ): DomainResult<Unit> = withContext(dispatcher.io) {
        val request = RemovePromoProductRequest(
            idToko = idToko,
            idProduk = idProduk
        )

        val result = apiService.removePromoFromProduct(request)

        return@withContext processResponse(result) {
            dao.updateProdukTokoById(
                idToko = idToko,
                idProduk = idProduk,
                hargaPromo = 0
            )
            DomainResult.Success(Unit)
        }
    }

    override suspend fun removeProductFromStore(
        idToko: Int,
        idProduk: Int
    ): DomainResult<Unit> = withContext(dispatcher.io) {
        val request = RemovePromoProductRequest(
            idToko = idToko,
            idProduk = idProduk
        )

        val result = apiService.removeProductFromStore(request)

        return@withContext processResponse(result) {
            dao.deleteProdukToko(idToko, idProduk)
            DomainResult.Success(Unit)
        }
    }
}
