package com.arfdevs.productmonitoring.data.local.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.arfdevs.productmonitoring.helper.Column
import com.arfdevs.productmonitoring.helper.Local

@Entity(tableName = Local.ATTENDANCE)
data class AttendanceEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = Column.ID)
    val id: Int = 0,

    @ColumnInfo(name = Column.STATUS)
    val status: String = "",

    @ColumnInfo(name = Column.USERNAME)
    val barcode: String = "",

    @ColumnInfo(name = Column.CREATED_AT)
    val createdAt: Long = System.currentTimeMillis()
)
