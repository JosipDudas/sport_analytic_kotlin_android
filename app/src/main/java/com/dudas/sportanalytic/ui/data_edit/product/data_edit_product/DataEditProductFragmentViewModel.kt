package com.dudas.sportanalytic.ui.data_edit.product.data_edit_product

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.dudas.sportanalytic.api.SportAnalyticService
import com.dudas.sportanalytic.api.response.GetProductCategoriesResponse
import com.dudas.sportanalytic.database.SportAnalyticDB
import com.dudas.sportanalytic.database.entities.ProductCategories
import com.dudas.sportanalytic.preferences.MyPreferences
import com.dudas.sportanalytic.ui.BaseViewModel
import kotlinx.coroutines.launch
import ru.gildor.coroutines.retrofit.awaitResponse
import javax.inject.Inject

class DataEditProductFragmentViewModel @Inject constructor(val connector: SportAnalyticDB,
                                                           val preferences: MyPreferences,
                                                           val sportAnalyticService: SportAnalyticService
) : BaseViewModel() {
    val products = MutableLiveData<GetProductCategoriesResponse>()

    fun fetchProductData() {
        coroutineScope.launch {
            getProduct()
        }
    }

    suspend fun getProduct() {
        progress.postValue(true)
        try {
            val response = sportAnalyticService
                .getProductCategories(preferences.getLocation()!!.id)
                .awaitResponse()
                .body()
            if (response!!.status) {
                for (i in 0 until response.productCategories!!.size) {
                    connector.productCategoriesDao().insertProductCategories(
                        ProductCategories(
                            response.productCategories[i].id,
                            response.productCategories[i].name,
                            response.productCategories[i].description,
                            response.productCategories[i].location_id
                        )
                    )
                }
            } else {
                error.postValue(IllegalStateException(response.message))
            }
        }catch (e: Exception) {
            error.postValue(e)
        }finally {
            progress.postValue(false)
        }
    }
}

class DataEditProductsFragmentViewModelFactory @Inject constructor(val connector: SportAnalyticDB,
                                                               val preferences: MyPreferences,
                                                               val sportAnalyticService: SportAnalyticService
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return DataEditProductFragmentViewModel(connector, preferences, sportAnalyticService) as T
    }
}