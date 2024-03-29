package com.dudas.sportanalytic.api.response

import com.google.gson.annotations.SerializedName

class CreateUserResponse : BaseResponse() {
    @SerializedName("id")
    val id: String = ""
    @SerializedName("firstname")
    val firstname: String = ""
    @SerializedName("lastname")
    val lastname: String = ""
    @SerializedName("password")
    val password: String = ""
    @SerializedName("email")
    val email: String = ""
    @SerializedName("position")
    val position: String = ""
    @SerializedName("address")
    val address: String = ""
    @SerializedName("sex")
    val sex: String = ""
}