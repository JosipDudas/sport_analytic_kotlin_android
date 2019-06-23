package com.dudas.sportanalytic.ui.reservations

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.dudas.sportanalytic.api.SportAnalyticService
import com.dudas.sportanalytic.database.SportAnalyticDB
import com.dudas.sportanalytic.database.entities.ProductCategories
import com.dudas.sportanalytic.database.entities.Reservation
import com.dudas.sportanalytic.database.entities.ReservationItem
import com.dudas.sportanalytic.preferences.MyPreferences
import com.dudas.sportanalytic.ui.BaseViewModel
import kotlinx.coroutines.launch
import ru.gildor.coroutines.retrofit.awaitResponse
import java.util.*
import javax.inject.Inject

class ReservationFragmentViewModel @Inject constructor(val sportAnalyticService: SportAnalyticService,
                                                       val connector: SportAnalyticDB,
                                                       val preferences: MyPreferences): BaseViewModel(){

    var reservation = MutableLiveData<Reservation>().default(Reservation(
        id = UUID.randomUUID().toString().toUpperCase(),
        date = Calendar.getInstance().time,
        location_id = preferences.getLocation()?.id!!,
        description = ""))

    private var reservationItem = MutableLiveData<ReservationItem>().default(ReservationItem(
        reservation_id = reservation.value!!.id,
        product_id = "",
        id = UUID.randomUUID().toString().toUpperCase(),
        from = Calendar.getInstance().time,
        to = Calendar.getInstance().time,
        quantity = 0))
    var user = MutableLiveData<Boolean>().default(true)

    private var reservationItemList: MutableList<ReservationItem> = mutableListOf()

    private var total: Double = 0.toDouble()
    private var reservationItemId: String? = null
    var productsList = MutableLiveData<List<ProductCategories>>()

    var buttonView = MutableLiveData<Boolean>()
    var lastSelection = MutableLiveData<LastSelection>()
    var popupWindowIsOpen = MutableLiveData<Boolean>()

    private fun <T : Any?> MutableLiveData<T>.default(initialValue: T) = apply { setValue(initialValue) }

    fun onCreate() {
        if(preferences.getUser()!= null) {
            coroutineScope.launch {
                getProduct()
            }
        } else {
            productsList.postValue(null)
        }
    }

    suspend fun getProduct() {
        progress.postValue(true)
        try {
            val response = sportAnalyticService
                .getProductCategories(preferences.getLocation()!!.id)
                .awaitResponse()
                .body()
            val allProductCategoriesInLocalDB = connector.productCategoriesDao().getAllProductCategories()
            if (response!!.status) {
                for (i in 0 until response.productCategories!!.size) {
                    var exist = false
                    for (j in 0 until allProductCategoriesInLocalDB.size) {
                        if (allProductCategoriesInLocalDB[j].id == response.productCategories[i].id) {
                            exist = true
                        }
                    }
                    if (!exist) {
                        connector.productCategoriesDao().insertProductCategories(ProductCategories(
                            response.productCategories[i].id,
                            response.productCategories[i].name,
                            response.productCategories[i].description,
                            response.productCategories[i].location_id
                        ))
                        exist = false
                    }
                }
            }
            productsList.postValue(connector.productCategoriesDao().getProductCategoriesForLocation(preferences.getLocation()!!.id))
        }catch (e: Exception) {
            error.postValue(e)
        }finally {
            progress.postValue(false)
        }
    }

    fun onResume() {
        if (buttonView.value == true) {
            //setLastSelectionAndSetTotal(getProductName(), getReservationItemQuantity())
            buttonView.postValue(true)
        } else {
            buttonView.postValue(false)
        }
    }
}
class LastSelection(val productName: String, val quantityProduct: String, val total: String)

class ReservationFragmentViewModelFactory @Inject constructor(val sportAnalyticService: SportAnalyticService,
                                                          val connector: SportAnalyticDB,
                                                          val preferences: MyPreferences) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return ReservationFragmentViewModel(sportAnalyticService, connector, preferences) as T
    }
}