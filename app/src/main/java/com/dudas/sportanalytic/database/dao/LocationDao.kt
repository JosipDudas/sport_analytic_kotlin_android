package com.dudas.sportanalytic.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.dudas.sportanalytic.database.entities.Location
import com.dudas.sportanalytic.database.entities.User

@Dao
abstract class LocationDao {
    @Query("SELECT * FROM locations")
    abstract fun getAllLocations(): List<Location>

    @Query("DELETE FROM locations")
    abstract fun deleteAll()

    @Query("DELETE FROM locations WHERE id=:locationId")
    abstract fun delete(locationId: String)

    @Insert
    abstract
    fun insertLocation(location: Location)

    @Update
    abstract
    fun updateLocation(location: Location)
}