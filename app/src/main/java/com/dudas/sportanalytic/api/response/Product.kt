package com.dudas.sportanalytic.api.response

import com.google.gson.annotations.SerializedName

data class Product(
    @SerializedName("id")
    var id: String,
    @SerializedName("name")
    var name: String,
    @SerializedName("categorie_id")
    var categorie_id: String
)