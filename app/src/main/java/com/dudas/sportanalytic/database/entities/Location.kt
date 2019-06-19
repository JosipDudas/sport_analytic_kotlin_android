package com.dudas.sportanalytic.database.entities

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.dudas.sportanalytic.constants.DBConstants
import com.google.gson.annotations.SerializedName

@Entity(tableName = DBConstants.LOCATION)
data class Location(
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
    @SerializedName("company_id")
    @ColumnInfo(name = "company_id")
    var company_id: String? = null
)