package com.dudas.sportanalytic.api.response

import com.google.gson.annotations.SerializedName

class UpdateProductResponse: BaseResponse() {
    @SerializedName("id")
    val id: String = ""
    @SerializedName("name")
    val name: String = ""
    @SerializedName("categorie_id")
    val categorie_id: String = ""
}