package com.arfdevs.productmonitoring.data.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.arfdevs.productmonitoring.data.local.db.entity.ProdukEntity
import com.arfdevs.productmonitoring.data.local.db.entity.ProdukTokoEntity
import com.arfdevs.productmonitoring.data.local.db.entity.TokoEntity

@Database(
    entities = [
        ProdukEntity::class,
        TokoEntity::class,
        ProdukTokoEntity::class
    ],
    version = 1
)
abstract class Database: RoomDatabase() {

    abstract fun Dao(): Dao

}
