package com.arfdevs.productmonitoring.data.remote.response

import com.google.gson.annotations.SerializedName

data class ListProdukResponse(
    @field:SerializedName("products") val products: List<Produk?>? = null,
)

data class Produk(
    @field:SerializedName("nama_produk") val namaProduk: String? = null,
    @field:SerializedName("harga") val harga: Int? = null,
    @field:SerializedName("id") val id: Int? = null,
    @field:SerializedName("barcode") val barcode: String? = null
)
