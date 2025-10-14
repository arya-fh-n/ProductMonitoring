package com.arfdevs.productmonitoring.domain.repository

import com.arfdevs.productmonitoring.helper.DomainResult

interface ReportRepository {
    suspend fun reportAttendance(status: String): DomainResult<Boolean>

    suspend fun getAttendance(): DomainResult<Boolean>

    suspend fun reportProduct(
        idToko: Int,
        idProduk: Int
    ): DomainResult<Unit>

    suspend fun reportPromo(
        idToko: Int,
        idProduk: Int,
        hargaPromo: Int
    ): DomainResult<Unit>

    suspend fun removePromo(
        idToko: Int,
        idProduk: Int,
    ): DomainResult<Unit>

    suspend fun removeProductFromStore(
        idToko: Int,
        idProduk: Int,
    ): DomainResult<Unit>

}
