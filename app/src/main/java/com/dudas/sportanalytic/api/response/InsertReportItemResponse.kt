package com.dudas.sportanalytic.api.response

import com.google.gson.annotations.SerializedName

class InsertReportItemResponse : BaseResponse() {
    @SerializedName("id")
    val id: String = ""
    @SerializedName("report_id")
    val report_id: String = ""
    @SerializedName("product_id")
    val product_id: String = ""
    @SerializedName("quantity")
    val quantity: Int = 1
}