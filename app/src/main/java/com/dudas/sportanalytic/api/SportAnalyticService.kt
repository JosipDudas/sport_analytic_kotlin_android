package com.dudas.sportanalytic.api

import com.dudas.sportanalytic.api.response.CreateUserResponse
import com.dudas.sportanalytic.api.response.GetCompaniesResponse
import com.dudas.sportanalytic.api.response.GetLocationsResponse
import com.dudas.sportanalytic.api.response.GetLoginResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface SportAnalyticService {
    companion object {
        const val REFRESH_ENDPOINT = "/.auth/refresh"
    }

    @GET("/api/user/login.php")
    fun userLogin(@Query("email") email: String, @Query("password") password: String?): Call<GetLoginResponse>

    @GET("/api/user/companies.php")
    fun getCompanies(): Call<GetCompaniesResponse>

    @GET("/api/location/locations.php")
    fun getLocations(@Query("company_id") company_id: String): Call<GetLocationsResponse>

    @POST("/api/user/signup.php")
    fun createUser(@Query("id") id: String, @Query("firstname") firstname: String?,
                   @Query("lastname") lastname: String, @Query("password") password: String?,
                   @Query("email") email: String, @Query("position") position: String?,
                   @Query("address") address: String, @Query("sex") sex: String?,
                   @Query("company_id") company_id: String): Call<CreateUserResponse>
}