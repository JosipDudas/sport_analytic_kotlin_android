package com.dudas.sportanalytic.api.response

import com.google.gson.annotations.SerializedName

data class ProductCategories(
    @SerializedName("id")
    var id: String,
    @SerializedName("name")
    var name: String,
    @SerializedName("description")
    var description: String,
    @SerializedName("location_id")
    var location_id: String
)