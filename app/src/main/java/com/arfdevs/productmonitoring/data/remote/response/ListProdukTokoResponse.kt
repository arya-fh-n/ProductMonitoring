package com.arfdevs.productmonitoring.data.remote.response

import com.google.gson.annotations.SerializedName

data class ListProdukTokoResponse(
	@field:SerializedName("products") val products: List<ProdukItem?>? = null
)

data class ProdukItem(
	@field:SerializedName("id") val id: Int? = null,
	@field:SerializedName("nama_produk") val namaProduk: String? = null,
	@field:SerializedName("harga") val harga: Int? = null,
	@field:SerializedName("harga_promo") val hargaPromo: Int? = null,
	@field:SerializedName("barcode") val barcode: String? = null
)
