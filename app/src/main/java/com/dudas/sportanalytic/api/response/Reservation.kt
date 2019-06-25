package com.dudas.sportanalytic.api.response

import com.google.gson.annotations.SerializedName
import java.sql.Date

data class Reservation(
    @SerializedName("id")
    var id: String,
    @SerializedName("from")
    var from: Date,
    @SerializedName("to")
    var to: Date,
    @SerializedName("location_id")
    var location_id: String,
    @SerializedName("description")
    var description: String
)