package com.dudas.sportanalytic.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.dudas.sportanalytic.database.entities.Reservation

@Dao
abstract class ReservationDao {
    @Query("SELECT * FROM reservations ORDER BY `from` ASC")
    abstract fun getAllReservations(): List<Reservation>

    @Query("SELECT * FROM reservations WHERE location_id=:categorieId ORDER BY `from` ASC")
    abstract fun getSpecificReservations(categorieId: String): List<Reservation>

    @Query("SELECT * FROM reservations WHERE id=:reservationId ORDER BY `from` ASC")
    abstract fun getSpecificReservation(reservationId: String): Reservation

    @Query("DELETE FROM reservations")
    abstract fun deleteAll()

    @Query("DELETE FROM reservations WHERE id=:reservationId")
    abstract fun delete(reservationId: String)

    @Insert
    abstract
    fun insertReservation(reservation: Reservation)

    @Update
    abstract
    fun updateReservation(reservation: Reservation)
}