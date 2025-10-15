package com.arfdevs.productmonitoring.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class TokoModel(
    val id: Int = 0,
    val namaToko: String = "",
    val kodeToko: String = "",
    val alamat: String = ""
): Parcelable
