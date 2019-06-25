package com.dudas.sportanalytic.api.response

import com.google.gson.annotations.SerializedName
import java.util.*

class UpdateReservationResponse : BaseResponse() {
    @SerializedName("id")
    val id: String = ""
    @SerializedName("from")
    val from: Date? = null
    @SerializedName("to")
    val to: Date? = null
    @SerializedName("location_id")
    val location_id: String = ""
    @SerializedName("description")
    val description: String = ""
}