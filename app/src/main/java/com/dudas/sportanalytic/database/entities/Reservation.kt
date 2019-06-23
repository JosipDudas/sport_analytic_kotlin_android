package com.dudas.sportanalytic.database.entities

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.dudas.sportanalytic.constants.DBConstants
import com.dudas.sportanalytic.utils.DateNotNullConverter
import com.google.gson.annotations.SerializedName
import java.io.FileDescriptor
import java.util.*

@Entity(tableName = DBConstants.RESERVATION)
class Reservation(id: String = UUID.randomUUID().toString().toUpperCase(),
                       date: Date? = null,
                       location_id: String? = null,
                       description: String? = null) {
    @ColumnInfo(name = "id")
    @NonNull
    @PrimaryKey
    var id: String = id

    @ColumnInfo(name = "date")
    @TypeConverters(DateNotNullConverter::class)
    var date: Date? = date

    @ColumnInfo(name = "location_id")
    var location_id: String? = location_id

    @ColumnInfo(name = "description")
    var description: String? = description
}