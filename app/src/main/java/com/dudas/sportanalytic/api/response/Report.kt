package com.dudas.sportanalytic.api.response

import com.google.gson.annotations.SerializedName
import java.sql.Date

data class Report(
    @SerializedName("id")
    var id: String,
    @SerializedName("date")
    var date: Date,
    @SerializedName("location_id")
    var location_id: String
)