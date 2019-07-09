package com.dudas.sportanalytic.api.response

import com.google.gson.annotations.SerializedName

class GetReportResponse: BaseResponse() {
    @field:SerializedName("report")
    val report: List<Report>? = null
}