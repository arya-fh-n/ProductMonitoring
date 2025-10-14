package com.arfdevs.productmonitoring.data.local.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.arfdevs.productmonitoring.helper.Column
import com.arfdevs.productmonitoring.helper.Local

@Entity(tableName = Local.PRODUK_TOKO)
data class ProdukTokoEntity(
    @PrimaryKey
    @ColumnInfo(name = Column.ID)
    val id: Int = 0,

    @ColumnInfo(name = Column.ID_TOKO)
    val idToko: Int = 0,

    @ColumnInfo(name = Column.ID_PRODUK)
    val idProduk: Int = 0,

    @ColumnInfo(name = Column.NAMA_PRODUK)
    val namaProduk: String = "",

    @ColumnInfo(name = Column.HARGA)
    val harga: Int = 0,

    @ColumnInfo(name = Column.HARGA_PROMO)
    val hargaPromo: Int = 0,

    @ColumnInfo(name = Column.BARCODE)
    val barcode: String = ""
)
