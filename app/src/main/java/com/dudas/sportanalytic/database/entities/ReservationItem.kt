package com.dudas.sportanalytic.database.entities

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.dudas.sportanalytic.constants.DBConstants
import java.util.*

@Entity(tableName = DBConstants.RESERVATION_ITEM)
class ReservationItem(id: String=UUID.randomUUID().toString().toUpperCase(),
                      product_id: String? =null,
                      reservation_id: String? =null) {
    @ColumnInfo(name = "id")
    @NonNull
    @PrimaryKey
    var id: String = id

    @ColumnInfo(name = "product_id")
    var product_id: String? = product_id

    @ColumnInfo(name = "reservation_id")
    var reservation_id: String? = reservation_id
}