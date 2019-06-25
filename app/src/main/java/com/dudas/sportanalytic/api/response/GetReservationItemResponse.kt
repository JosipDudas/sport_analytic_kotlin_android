package com.dudas.sportanalytic.api.response

import com.google.gson.annotations.SerializedName

class GetReservationItemResponse : BaseResponse() {
    @field:SerializedName("reservationItem")
    val reservationItem: List<ReservationItem>? = null
}