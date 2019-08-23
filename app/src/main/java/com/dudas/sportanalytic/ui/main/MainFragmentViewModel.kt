package com.dudas.sportanalytic.ui.main

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.dudas.sportanalytic.api.SportAnalyticService
import com.dudas.sportanalytic.api.response.GetLocationsResponse
import com.dudas.sportanalytic.api.response.Locations
import com.dudas.sportanalytic.database.SportAnalyticDB
import com.dudas.sportanalytic.database.entities.Product
import com.dudas.sportanalytic.database.entities.ProductCategories
import com.dudas.sportanalytic.database.entities.Reservation
import com.dudas.sportanalytic.database.entities.ReservationItem
import com.dudas.sportanalytic.preferences.Location
import com.dudas.sportanalytic.preferences.MyPreferences
import com.dudas.sportanalytic.ui.BaseViewModel
import com.dudas.sportanalytic.utils.fromReservationDateToDate
import com.dudas.sportanalytic.utils.getDateFormatForReservationDate
import kotlinx.coroutines.launch
import ru.gildor.coroutines.retrofit.awaitResponse
import java.sql.Date
import java.util.*
import javax.inject.Inject

class MainFragmentViewModel @Inject constructor(val preferences: MyPreferences,
                                                    val connector: SportAnalyticDB,
                                                    val sportAnalyticService: SportAnalyticService
): BaseViewModel() {

    var createAdapter = MutableLiveData<Boolean>()

    fun getReservations() {
        coroutineScope.launch {
            getAllReservationsForThisLocation()
        }
    }

    suspend fun getAllReservationsForThisLocation() {
        progress.postValue(true)
        try {
            connector.reservationItemDao().deleteAll()
            connector.reservationDao().deleteAll()

            val responseReservation = sportAnalyticService
                .getReservations(preferences.getLocation()!!.id)
                .awaitResponse()
                .body()
            val allReservationsLocaly = connector.reservationDao().getAllReservations()
            if (responseReservation!!.status) {
                for (i in 0 until responseReservation.reservation!!.size) {
                    var exist = false
                    for (j in 0 until allReservationsLocaly.size) {
                        if (responseReservation.reservation[i].id == allReservationsLocaly[j].id) {
                            exist = true
                        }
                    }
                    if (!exist) {
                        connector.reservationDao().insertReservation(
                            Reservation(
                                id = responseReservation.reservation[i].id,
                                from = responseReservation.reservation[i].from,
                                to = responseReservation.reservation[i].to,
                                location_id = responseReservation.reservation[i].location_id,
                                description = responseReservation.reservation[i].description))
                    }

                    val responseReservationItems = sportAnalyticService
                        .getReservationItems(responseReservation.reservation[i].id)
                        .awaitResponse()
                        .body()
                    val allReservationItemsLocaly = connector.reservationItemDao().getAllReservationItems()
                    if (responseReservationItems!!.status) {
                        for (k in 0 until responseReservationItems.reservationItem!!.size) {
                            var exists = false
                            for (l in 0 until allReservationItemsLocaly.size) {
                                if (responseReservationItems.reservationItem[k].id == allReservationItemsLocaly[l].id) {
                                    exists = true
                                }
                            }
                            if (!exists) {
                                connector.reservationItemDao().insertReservationItem(
                                    ReservationItem(
                                        id = responseReservationItems.reservationItem[k].id,
                                        product_id = responseReservationItems.reservationItem[k].product_id,
                                        reservation_id = responseReservationItems.reservationItem[k].reservation_id))
                            }
                        }
                    }
                }
            }

            val response = sportAnalyticService
                .getProductCategories(preferences.getLocation()!!.id)
                .awaitResponse()
                .body()
            val allProductcategoriesInLocalDB = connector.productCategoriesDao().getAllProductCategories()
            if (response!!.status) {
                for (i in 0 until response.productCategories!!.size) {
                    var exist = false
                    for (j in 0 until allProductcategoriesInLocalDB.size) {
                        if (allProductcategoriesInLocalDB[j].id == response.productCategories[i].id) {
                            exist = true
                        }
                    }
                    if (!exist) {
                        connector.productCategoriesDao().insertProductCategories(
                            ProductCategories(
                                response.productCategories[i].id,
                                response.productCategories[i].name,
                                response.productCategories[i].description,
                                response.productCategories[i].location_id
                            )
                        )
                    }

                    val responseProducts = sportAnalyticService
                        .getProduct(response.productCategories[i].id)
                        .awaitResponse()
                        .body()
                    val allProductInLocalDB = connector.productDao().getAllProducts()
                    if (responseProducts!!.status) {
                        for (k in 0 until responseProducts.product!!.size) {
                            var exists = false
                            for (l in 0 until allProductInLocalDB.size) {
                                if (allProductInLocalDB[l].id == responseProducts.product[k].id) {
                                    exists = true
                                }
                            }
                            if (!exists) {
                                connector.productDao().insertProduct(
                                    Product(
                                        responseProducts.product[k].id,
                                        responseProducts.product[k].name,
                                        responseProducts.product[k].categorie_id
                                    )
                                )
                            }
                        }
                    }

                }
            }
        }catch (e: Exception) {
            error.postValue(e)
        }finally {
            createAdapter.postValue(true)
            progress.postValue(false)
        }
    }

    fun setAdapterToFalse() {
        createAdapter.postValue(false)
    }

    fun getReservationList(): List<Reservation> {
        val reservationsList = connector.reservationDao().getAllReservations()
        val reservationMutableList = mutableListOf<Reservation>()
        val cldr = Calendar.getInstance()
        reservationsList.forEach {
            val current = Date.valueOf(getDateFormatForReservationDate(cldr.time))
            if (Date.valueOf(getDateFormatForReservationDate(cldr.time)) >= it.from &&
                Date.valueOf(getDateFormatForReservationDate(cldr.time)) <= it.to
            ) {
                reservationMutableList.add(it)
            }
        }
        return reservationMutableList
    }
}

class MainFragmentViewModelFactory @Inject constructor(val preferences: MyPreferences,
                                                           val connector: SportAnalyticDB,
                                                           val sportAnalyticService: SportAnalyticService
):
    ViewModelProvider.Factory{
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return MainFragmentViewModel(preferences, connector, sportAnalyticService) as T
    }
}