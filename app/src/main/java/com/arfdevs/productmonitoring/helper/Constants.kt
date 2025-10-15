package com.arfdevs.productmonitoring.helper

object Constants {

    object DB {
        const val DATABASE = "product_monitoring_db"
        const val PRODUK = "produk"
        const val TOKO = "toko"
        const val PRODUK_TOKO = "produk_toko"
        const val ATTENDANCE = "attendance"

        const val DATA_STORE = "auth_prefs"
        const val AUTH_TOKEN = "auth_token"
        const val USERNAME = "username"
    }

    object Column {
        const val ID = "id"
        const val ID_PRODUK = "id_produk"
        const val ID_TOKO = "id_toko"

        const val NAMA_PRODUK = "nama_produk"
        const val HARGA = "harga"
        const val BARCODE = "barcode"

        const val NAMA_TOKO = "nama_toko"
        const val KODE_TOKO = "kode_toko"
        const val ALAMAT = "alamat"

        const val HARGA_PROMO = "harga_promo"

        const val STATUS = "status"
        const val USERNAME = "username"
        const val CREATED_AT = "created_at"

        const val IS_DIRTY = "is_dirty"
    }

    const val HADIR = "hadir"
    const val ABSEN = "absen"

    const val USERNAME_MIN_LENGTH = 4
    const val PASSWORD_MIN_LENGTH = 4

    const val SILENT_NAV_CODE = 900

    const val EXTRA_TOKO = "extra_toko"
    const val EXTRA_ID_TOKO = "extra_id_toko"
    const val EXTRA_NAMA_TOKO = "extra_nama_toko"

    const val EXTRA_BS_PRODUK = "extra_bs_produk"
    const val EXTRA_BS_ID_TOKO = "extra_bs_id_toko"
    const val EXTRA_BS_IS_AVAILABLE = "extra_bs_is_available"

}

typealias Local = Constants.DB
typealias Column = Constants.Column
