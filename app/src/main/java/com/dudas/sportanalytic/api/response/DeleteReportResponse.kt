package com.dudas.sportanalytic.api.response

import com.google.gson.annotations.SerializedName
import java.util.*

class DeleteReportResponse : BaseResponse() {
    @SerializedName("id")
    val id: String = ""
    @SerializedName("date")
    val date: Date? = null
    @SerializedName("location_id")
    val location_id: String = ""
}