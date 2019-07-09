package com.dudas.sportanalytic.api.response

import com.google.gson.annotations.SerializedName

class GetReportItemResponse : BaseResponse() {
    @field:SerializedName("reportItem")
    val reportItem: List<ReportItem>? = null
}