package com.dudas.sportanalytic.api.response

import com.google.gson.annotations.SerializedName

data class ReservationItem(
    @SerializedName("id")
    var id: String,
    @SerializedName("product_id")
    var product_id: String,
    @SerializedName("reservation_id")
    var reservation_id: String
)