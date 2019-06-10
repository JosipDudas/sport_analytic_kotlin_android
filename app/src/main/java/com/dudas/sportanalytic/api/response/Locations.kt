package com.dudas.sportanalytic.api.response

import com.google.gson.annotations.SerializedName

data class Locations(
    @SerializedName("id")
    var id: String,
    @SerializedName("name")
    var name: String,
    @SerializedName("description")
    var description: String,
    @SerializedName("company_id")
    var company_id: String
)