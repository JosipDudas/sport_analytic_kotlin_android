package com.dudas.sportanalytic.database.entities

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.dudas.sportanalytic.constants.DBConstants
import com.google.gson.annotations.SerializedName

@Entity(tableName = DBConstants.USERS)
data class User(
    @SerializedName("id")
    @ColumnInfo(name = "id")
    @NonNull
    @PrimaryKey
    var id: String = "",
    @SerializedName("firstname")
    @ColumnInfo(name = "firstname")
    var firstname: String? = null,
    @SerializedName("lastname")
    @ColumnInfo(name = "lastname")
    var lastname: String? = null,
    @SerializedName("email")
    @ColumnInfo(name = "email")
    var email: String? = null,
    @SerializedName("password")
    @ColumnInfo(name = "password")
    var password: String? = null,
    @SerializedName("position")
    @ColumnInfo(name = "position")
    var position: String? = null,
    @SerializedName("address")
    @ColumnInfo(name = "address")
    var address: String? = null
)