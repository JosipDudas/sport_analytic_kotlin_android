package com.dudas.sportanalytic.database.entities

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.dudas.sportanalytic.constants.DBConstants
import com.dudas.sportanalytic.utils.DateNotNullConverter
import com.google.gson.annotations.SerializedName
import java.util.*

@Entity(tableName = DBConstants.RESERVATION_ITEM)
class ReservationItem(id: String=UUID.randomUUID().toString().toUpperCase(),
                      product_id: String? =null,
                      reservation_id: String? =null,
                      from: Date? =null,
                      to: Date? =null,
                      quantity: Int? =null) {
    @ColumnInfo(name = "id")
    @NonNull
    @PrimaryKey
    var id: String = id

    @ColumnInfo(name = "product_id")
    var product_id: String? = product_id

    @ColumnInfo(name = "reservation_id")
    var reservation_id: String? = reservation_id

    @ColumnInfo(name = "from")
    @TypeConverters(DateNotNullConverter::class)
    var from: Date? = from

    @ColumnInfo(name = "to")
    @TypeConverters(DateNotNullConverter::class)
    var to: Date? = to

    @ColumnInfo(name = "quantity")
    var quantity: Int? = quantity
}