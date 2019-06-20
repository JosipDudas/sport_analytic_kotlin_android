package com.dudas.sportanalytic.api.response

import com.google.gson.annotations.SerializedName

class UpdateProductCategoriesResponse: BaseResponse() {
    @SerializedName("id")
    val id: String = ""
    @SerializedName("name")
    val name: String = ""
    @SerializedName("description")
    val description: String = ""
    @SerializedName("location_id")
    val location_id: String = ""
}