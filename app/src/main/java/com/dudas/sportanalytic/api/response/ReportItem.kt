package com.dudas.sportanalytic.api.response

import com.google.gson.annotations.SerializedName

data class ReportItem(
    @SerializedName("id")
    var id: String,
    @SerializedName("report_id")
    var report_id: String,
    @SerializedName("product_id")
    var product_id: String,
    @SerializedName("quantity")
    var quantity: Int
)