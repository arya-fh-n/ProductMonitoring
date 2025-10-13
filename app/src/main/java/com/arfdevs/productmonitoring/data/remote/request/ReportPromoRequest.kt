package com.arfdevs.productmonitoring.data.remote.request

import com.google.gson.annotations.SerializedName

data class ReportPromoRequest(
    @SerializedName("id_toko") val idToko: Int,
    @SerializedName("id_produk") val idProduk: Int,
    @SerializedName("harga_promo") val hargaPromo: Int
)
