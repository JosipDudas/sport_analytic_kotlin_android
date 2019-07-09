package com.dudas.sportanalytic.database.entities

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.dudas.sportanalytic.constants.DBConstants
import java.util.*

@Entity(tableName = DBConstants.REPORT_ITEM)
class ReportItem(id: String= UUID.randomUUID().toString().toUpperCase(),
                 product_id: String? =null,
                 report_id: String? =null,
                 quantity: Int? = null) {
    @ColumnInfo(name = "id")
    @NonNull
    @PrimaryKey
    var id: String = id

    @ColumnInfo(name = "product_id")
    var product_id: String? = product_id

    @ColumnInfo(name = "report_id")
    var report_id: String? = report_id

    @ColumnInfo(name = "quantity")
    var quantity: Int? = quantity
}