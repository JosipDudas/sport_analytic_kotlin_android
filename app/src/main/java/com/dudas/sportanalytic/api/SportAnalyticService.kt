package com.dudas.sportanalytic.api

import com.dudas.sportanalytic.api.response.*
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

    @GET("/api/location/delete_location.php")
    fun deleteLocations(@Query("id") company_id: String): Call<DeleteLocationsResponse>

    @POST("/api/location/create_location.php")
    fun insertLocations(@Query("id") id: String,
                        @Query("name") name: String,
                        @Query("description") description: String,
                        @Query("company_id") company_id: String): Call<InsertLocationResponse>

    @POST("/api/location/update_location.php")
    fun updateLocations(@Query("id") id: String,
                        @Query("name") name: String,
                        @Query("description") description: String,
                        @Query("company_id") company_id: String): Call<UpdateLocationResponse>

    @GET("/api/product_categories/product_categories.php")
    fun getProductCategories(@Query("location_id") location_id: String): Call<GetProductCategoriesResponse>

    @GET("/api/product_categories/delete_product_categorie.php")
    fun deleteProductCategories(@Query("id") id: String): Call<DeleteProductCategoriesResponse>

    @POST("/api/product_categories/create_product_categorie.php")
    fun insertProductCategories(@Query("id") id: String,
                        @Query("name") name: String,
                        @Query("description") description: String,
                        @Query("location_id") location_id: String): Call<InsertProductCategoriesResponse>

    @POST("/api/product_categories/update_product_categorie.php")
    fun updateProductCategories(@Query("id") id: String,
                        @Query("name") name: String,
                        @Query("description") description: String,
                        @Query("location_id") location_id: String): Call<UpdateProductCategoriesResponse>

    @POST("/api/user/signup.php")
    fun createUser(@Query("id") id: String, @Query("firstname") firstname: String?,
                   @Query("lastname") lastname: String, @Query("password") password: String?,
                   @Query("email") email: String, @Query("position") position: String?,
                   @Query("address") address: String, @Query("sex") sex: String?,
                   @Query("company_id") company_id: String): Call<CreateUserResponse>
}