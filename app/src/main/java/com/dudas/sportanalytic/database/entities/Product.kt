package com.dudas.sportanalytic.database.entities

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.dudas.sportanalytic.constants.DBConstants
import com.google.gson.annotations.SerializedName

@Entity(tableName = DBConstants.PRODUCT)
data class Product(
    @SerializedName("id")
    @ColumnInfo(name = "id")
    @NonNull
    @PrimaryKey
    var id: String = "",
    @SerializedName("name")
    @ColumnInfo(name = "name")
    var name: String? = null,
    @SerializedName("categorie_id")
    @ColumnInfo(name = "categorie_id")
    var categorie_id: String? = null
)