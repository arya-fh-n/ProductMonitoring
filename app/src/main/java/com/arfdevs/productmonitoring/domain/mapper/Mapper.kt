package com.arfdevs.productmonitoring.domain.mapper

import com.arfdevs.productmonitoring.data.local.db.entity.ProdukEntity
import com.arfdevs.productmonitoring.data.local.db.entity.ProdukTokoEntity
import com.arfdevs.productmonitoring.data.local.db.entity.TokoEntity
import com.arfdevs.productmonitoring.domain.model.ProdukModel
import com.arfdevs.productmonitoring.domain.model.ProdukTokoModel
import com.arfdevs.productmonitoring.domain.model.TokoModel

interface Mapper {

    suspend fun mapProdukToDomain(produk: ProdukEntity): ProdukModel
    suspend fun mapTokoToDomain(toko: TokoEntity): TokoModel
    suspend fun mapProdukTokoToDomain(produkToko: ProdukTokoEntity): ProdukTokoModel

}