package com.dudas.sportanalytic.api.response

import com.google.gson.annotations.SerializedName

class GetProductResponse : BaseResponse() {
    @field:SerializedName("product")
    val product: List<Product>? = null
}