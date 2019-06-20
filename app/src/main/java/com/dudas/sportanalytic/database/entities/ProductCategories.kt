package com.dudas.sportanalytic.database.entities

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.dudas.sportanalytic.constants.DBConstants
import com.google.gson.annotations.SerializedName

@Entity(tableName = DBConstants.PRODUCT_CATEGORIES)
data class ProductCategories(
    @SerializedName("id")
    @ColumnInfo(name = "id")
    @NonNull
    @PrimaryKey
    var id: String = "",
    @SerializedName("name")
    @ColumnInfo(name = "name")
    var name: String? = null,
    @SerializedName("description")
    @ColumnInfo(name = "description")
    var description: String? = null,
    @SerializedName("location_id")
    @ColumnInfo(name = "location_id")
    var location_id: String? = null
)