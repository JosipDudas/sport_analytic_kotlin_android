package com.dudas.sportanalytic.api

import com.dudas.sportanalytic.api.response.*
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query
import java.util.*

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

    @GET("/api/products/products.php")
    fun getProduct(@Query("categorie_id") categorie_id: String): Call<GetProductResponse>

    @GET("/api/products/delete_product.php")
    fun deleteProduct(@Query("id") id: String): Call<DeleteProductResponse>

    @POST("/api/products/create_product.php")
    fun insertProduct(@Query("id") id: String,
                                @Query("name") name: String,
                                @Query("categorie_id") categorie_id: String): Call<InsertProductResponse>

    @POST("/api/products/update_product.php")
    fun updateProduct(@Query("id") id: String,
                      @Query("name") name: String,
                      @Query("categorie_id") categorie_id: String): Call<UpdateProductResponse>

    @POST("/api/user/signup.php")
    fun createUser(@Query("id") id: String, @Query("firstname") firstname: String?,
                   @Query("lastname") lastname: String, @Query("password") password: String?,
                   @Query("email") email: String, @Query("position") position: String?,
                   @Query("address") address: String, @Query("gender") gender: String?,
                   @Query("company_id") company_id: String): Call<CreateUserResponse>

    @GET("/api/reservation/reservations.php")
    fun getReservations(@Query("location_id") location_id: String): Call<GetReservationResponse>

    @GET("/api/reservation/delete_reservation.php")
    fun deleteReservation(@Query("id") id: String): Call<DeleteReservationResponse>

    @POST("/api/reservation/create_reservation.php")
    fun insertReservation(@Query("id") id: String,
                          @Query("from") from: String,
                          @Query("to") to: String,
                          @Query("location_id") location_id: String,
                          @Query("description") description: String): Call<InsertReservationResponse>

    @POST("/api/reservation/update_reservation.php")
    fun updateReservation(@Query("id") id: String,
                          @Query("from") from: String,
                          @Query("to") to: String,
                          @Query("location_id") location_id: String,
                          @Query("description") description: String): Call<UpdateReservationResponse>

    @GET("/api/reservation_items/reservation_item.php")
    fun getReservationItems(@Query("reservation_id") reservation_id: String): Call<GetReservationItemResponse>

    @GET("/api/reservation_items/delete_reservation_item.php")
    fun deleteReservationItem(@Query("id") id: String): Call<DeleteReservationItemResponse>

    @POST("/api/reservation_items/create_reservation_item.php")
    fun insertReservationItem(@Query("id") id: String,
                          @Query("product_id") product_id: String,
                          @Query("reservation_id") reservation_id: String): Call<InsertReservationItemResponse>

    @POST("/api/reservation_items/update_reservation_item.php")
    fun updateReservationItem(@Query("id") id: String,
                          @Query("product_id") product_id: String,
                          @Query("reservation_id") reservation_id: String): Call<UpdateReservationItemResponse>


    @GET("/api/reports/reports.php")
    fun getReports(@Query("location_id") location_id: String): Call<GetReportResponse>

    @GET("/api/reports/delete_report.php")
    fun deleteReport(@Query("id") id: String): Call<DeleteReportResponse>

    @POST("/api/reports/create_report.php")
    fun insertReport(@Query("id") id: String,
                          @Query("date") date: String,
                          @Query("location_id") location_id: String): Call<InsertReportResponse>

    @POST("/api/reports/update_report.php")
    fun updateReport(@Query("id") id: String,
                          @Query("date") date: String,
                          @Query("location_id") location_id: String): Call<UpdateReportResponse>

    @GET("/api/report_items/report_items.php")
    fun getReportItems(@Query("report_id") report_id: String): Call<GetReportItemResponse>

    @GET("/api/report_items/delete_report_items.php")
    fun deleteReportItem(@Query("id") id: String): Call<DeleteReportItemResponse>

    @POST("/api/report_items/create_report_items.php")
    fun insertReportItem(@Query("id") id: String,
                         @Query("report_id") report_id: String,
                         @Query("product_id") product_id: String,
                         @Query("quantity") quantity: Int): Call<InsertReportItemResponse>

    @POST("/api/report_items/update_report_items.php")
    fun updateReportItem(@Query("id") id: String,
                         @Query("report_id") report_id: String,
                         @Query("product_id") product_id: String,
                         @Query("quantity") quantity: Int): Call<UpdateReportItemResponse>
}