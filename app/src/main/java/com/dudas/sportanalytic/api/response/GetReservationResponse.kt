package com.dudas.sportanalytic.api.response

import com.google.gson.annotations.SerializedName

class GetReservationResponse : BaseResponse() {
    @field:SerializedName("reservation")
    val reservation: List<Reservation>? = null
}