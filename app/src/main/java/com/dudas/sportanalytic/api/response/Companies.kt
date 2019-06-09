package com.dudas.sportanalytic.api.response

import com.google.gson.annotations.SerializedName

data class Companies(
    @SerializedName("id")
    var id: String,
    @SerializedName("name")
    var name: String,
    @SerializedName("email")
    var email: String,
    @SerializedName("address")
    var address: String
)