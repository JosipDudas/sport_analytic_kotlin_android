package com.dudas.sportanalytic.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.dudas.sportanalytic.database.entities.ReservationItem

@Dao
abstract class ReservationItemDao {
    @Query("SELECT * FROM reservation_items ORDER BY product_id ASC")
    abstract fun getAllReservationItems(): List<ReservationItem>

    @Query("SELECT * FROM reservation_items WHERE reservation_id=:reservationId ORDER BY product_id ASC")
    abstract fun getSpecificReservationItems(reservationId: String): List<ReservationItem>

    @Query("SELECT * FROM reservation_items WHERE id=:reservationItemId ORDER BY product_id ASC")
    abstract fun getSpecificReservationItem(reservationItemId: String): ReservationItem

    @Query("DELETE FROM reservation_items")
    abstract fun deleteAll()

    @Query("DELETE FROM reservation_items WHERE id=:reservationId")
    abstract fun delete(reservationId: String)

    @Insert
    abstract
    fun insertReservationItem(reservation: ReservationItem)

    @Update
    abstract
    fun updateReservationItem(reservation: ReservationItem)
}