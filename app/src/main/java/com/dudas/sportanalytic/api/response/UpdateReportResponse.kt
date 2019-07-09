package com.dudas.sportanalytic.api.response

import com.google.gson.annotations.SerializedName
import java.util.*

class UpdateReportResponse: BaseResponse() {
    @SerializedName("id")
    val id: String = ""
    @SerializedName("date")
    val date: Date? = null
    @SerializedName("location_id")
    val location_id: String = ""
}