package com.arfdevs.productmonitoring.data.local.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.arfdevs.productmonitoring.data.local.db.entity.AttendanceEntity
import com.arfdevs.productmonitoring.data.local.db.entity.ProdukEntity
import com.arfdevs.productmonitoring.data.local.db.entity.ProdukTokoEntity
import com.arfdevs.productmonitoring.data.local.db.entity.TokoEntity
import com.arfdevs.productmonitoring.helper.Column
import com.arfdevs.productmonitoring.helper.Local

@Dao
interface Dao {

    // Produk table
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProduk(produk: ProdukEntity)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateProduk(produk: ProdukEntity)

    @Delete
    suspend fun deleteProduk(produk: ProdukEntity)

    @Query("SELECT * FROM ${Local.PRODUK}")
    fun getAllProduk(): List<ProdukEntity>

    // Toko table
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertToko(toko: TokoEntity)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateToko(toko: TokoEntity)

    @Delete
    suspend fun deleteToko(toko: TokoEntity)

    @Query("SELECT * FROM ${Local.TOKO}")
    fun getAllToko(): List<TokoEntity>

    // TokoProduk table
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProdukToko(produkToko: ProdukTokoEntity)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateProdukToko(produkToko: ProdukTokoEntity)

    @Query("UPDATE ${Local.PRODUK_TOKO} SET harga_promo = :hargaPromo, is_dirty = :isDirty WHERE id_toko = :idToko AND id_produk = :idProduk")
    suspend fun updateProdukTokoById(idToko: Int, idProduk: Int, hargaPromo: Int, isDirty: Boolean = false)

    @Query("DELETE FROM ${Local.PRODUK_TOKO} WHERE id_toko = :idToko AND id_produk = :idProduk")
    suspend fun deleteProdukToko(idToko: Int, idProduk: Int)

    @Query("SELECT * FROM ${Local.PRODUK_TOKO}")
    fun getAllProdukToko(): List<ProdukTokoEntity>

    @Query("SELECT * FROM ${Local.PRODUK_TOKO} WHERE ${Column.ID_TOKO} = :idToko")
    fun getAllProdukTokoById(idToko: Int): List<ProdukTokoEntity>

    @Query("SELECT * FROM ${Local.PRODUK_TOKO} WHERE is_dirty = 1")
    fun getAllProdukTokoDirty(): List<ProdukTokoEntity>

    @Query("UPDATE ${Local.PRODUK_TOKO} SET is_dirty = 0")
    suspend fun setAllProdukTokoClean()

    // Attendance table
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAttendance(attendance: AttendanceEntity)

    @Query("SELECT * FROM ${Local.ATTENDANCE} ORDER BY ${Column.CREATED_AT} DESC LIMIT 3")
    fun getRecentAttendance(): List<AttendanceEntity>
}
