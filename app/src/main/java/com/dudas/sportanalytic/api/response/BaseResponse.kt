package com.dudas.sportanalytic.api.response

import com.google.gson.annotations.SerializedName

abstract class BaseResponse{
    @SerializedName("status")
    val status:Boolean = false
    @SerializedName("message")
    val message:String = ""
}