package com.arfdevs.productmonitoring.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ProdukTokoModel(
    val id: Int = 0,
    val idProduk: Int = 0,
    val idToko: Int = 0,
    val namaProduk: String = "",
    val harga: Int = 0,
    val hargaPromo: Int = 0,
    val barcode: String = ""
) : Parcelable
