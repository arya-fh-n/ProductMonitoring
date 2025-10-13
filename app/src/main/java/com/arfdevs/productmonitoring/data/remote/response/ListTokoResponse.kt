package com.arfdevs.productmonitoring.data.remote.response

import com.google.gson.annotations.SerializedName

data class ListTokoResponse(
	@field:SerializedName("stores") val stores: List<Toko?>? = null,
)

data class Toko(
    @field:SerializedName("id") val id: Int? = null,
    @field:SerializedName("nama_toko") val namaToko: String? = null,
    @field:SerializedName("kode_toko") val kodeToko: String? = null,
    @field:SerializedName("alamat") val alamat: String? = null
)
