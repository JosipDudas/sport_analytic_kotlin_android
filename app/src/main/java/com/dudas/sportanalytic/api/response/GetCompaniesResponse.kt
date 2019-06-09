package com.dudas.sportanalytic.api.response

import com.google.gson.annotations.SerializedName

class GetCompaniesResponse : BaseResponse() {
    @field:SerializedName("companies")
    val companies: List<Companies>? = null
}