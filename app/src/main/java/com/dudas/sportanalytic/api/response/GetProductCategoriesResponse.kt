package com.dudas.sportanalytic.api.response

import com.google.gson.annotations.SerializedName

class GetProductCategoriesResponse : BaseResponse() {
    @field:SerializedName("product_categories")
    val productCategories: List<ProductCategories>? = null
}