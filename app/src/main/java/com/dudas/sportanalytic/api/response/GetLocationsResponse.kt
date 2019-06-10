package com.dudas.sportanalytic.api.response

import com.google.gson.annotations.SerializedName

class GetLocationsResponse : BaseResponse() {
    @field:SerializedName("locations")
    val locations: List<Locations>? = null
}