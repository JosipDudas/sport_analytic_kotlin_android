package com.dudas.sportanalytic.api.response

import com.google.gson.annotations.SerializedName

abstract class BaseResponse{
    @SerializedName("status")
    var status:Boolean = false
    @SerializedName("message")
    var message:String = ""
}