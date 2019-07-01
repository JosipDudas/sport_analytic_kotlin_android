package com.dudas.sportanalytic.api.response

import com.google.gson.annotations.SerializedName

class GetLoginResponse : BaseResponse() {
    @SerializedName("id")
    var id: String = ""
    @SerializedName("firstname")
    var firstname: String = ""
    @SerializedName("lastname")
    var lastname: String = ""
    @SerializedName("password")
    var password: String = ""
    @SerializedName("email")
    var email: String = ""
    @SerializedName("position")
    var position: String = ""
    @SerializedName("address")
    var address: String = ""
    @SerializedName("sex")
    var sex: String = ""
    @SerializedName("company_id")
    var company_id: String = ""
}