package com.dudas.sportanalytic.api.response

import com.google.gson.annotations.SerializedName
import java.sql.Date

class InsertReportResponse : BaseResponse() {
    @SerializedName("id")
    val id: String = ""
    @SerializedName("date")
    val date: Date? = null
    @SerializedName("location_id")
    val location_id: String = ""
}