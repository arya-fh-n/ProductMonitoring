package com.arfdevs.productmonitoring.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ProdukModel(
    val id: Int = 0,
    val namaProduk: String = "",
    val harga: Int = 0,
    val barcode: String = ""
) : Parcelable
