package com.arfdevs.productmonitoring.data.local.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.arfdevs.productmonitoring.helper.Column
import com.arfdevs.productmonitoring.helper.Local

@Entity(tableName = Local.TOKO)
data class TokoEntity(
    @PrimaryKey
    @ColumnInfo(name = Column.ID)
    val id: Int = 0,

    @ColumnInfo(name = Column.NAMA_TOKO)
    val namaToko: String = "",

    @ColumnInfo(name = Column.KODE_TOKO)
    val kodeToko: String = "",

    @ColumnInfo(name = Column.ALAMAT)
    val alamat: String = ""
)
