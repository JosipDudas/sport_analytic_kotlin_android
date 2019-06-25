package com.dudas.sportanalytic.api.response

import com.google.gson.annotations.SerializedName

class UpdateReservationItemResponse : BaseResponse() {
    @SerializedName("id")
    val id: String = ""
    @SerializedName("product_id")
    val product_id: String = ""
    @SerializedName("reservation_id")
    val reservation_id: String = ""
}