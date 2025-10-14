package com.arfdevs.productmonitoring.domain.mapper

import com.arfdevs.productmonitoring.data.local.db.entity.ProdukEntity
import com.arfdevs.productmonitoring.data.local.db.entity.ProdukTokoEntity
import com.arfdevs.productmonitoring.data.local.db.entity.TokoEntity
import com.arfdevs.productmonitoring.domain.model.ProdukModel
import com.arfdevs.productmonitoring.domain.model.ProdukTokoModel
import com.arfdevs.productmonitoring.domain.model.TokoModel
import com.arfdevs.productmonitoring.helper.CoroutinesDispatcherProvider
import kotlinx.coroutines.withContext

class MapperImpl(
    private val dispatcher: CoroutinesDispatcherProvider
) : Mapper {

    override suspend fun mapProdukToDomain(
        produk: ProdukEntity
    ): ProdukModel = withContext(dispatcher.io) {
        ProdukModel(
            id = produk.id,
            namaProduk = produk.namaProduk,
            harga = produk.harga,
            barcode = produk.barcode
        )
    }

    override suspend fun mapTokoToDomain(
        toko: TokoEntity
    ): TokoModel = withContext(dispatcher.io) {
        TokoModel(
            id = toko.id,
            namaToko = toko.namaToko,
            kodeToko = toko.kodeToko,
            alamat = toko.alamat
        )
    }

    override suspend fun mapProdukTokoToDomain(
        produkToko: ProdukTokoEntity
    ): ProdukTokoModel = withContext(dispatcher.io) {
        ProdukTokoModel(
            id = produkToko.id,
            idProduk = produkToko.idProduk,
            idToko = produkToko.idToko,
            namaProduk = produkToko.namaProduk,
            harga = produkToko.harga,
            hargaPromo = produkToko.hargaPromo,
            barcode = produkToko.barcode
        )
    }


}