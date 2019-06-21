package com.dudas.sportanalytic.ui.data_edit.product

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

class DataEditProductCategoriesFragmentViewModel @Inject constructor(val connector: SportAnalyticDB,
                                                            val preferences: MyPreferences,
                                                            val sportAnalyticService: SportAnalyticService) : BaseViewModel() {
    val deleteFab = MutableLiveData<Boolean>()
    val deleteConfirmFab = MutableLiveData<Boolean>()
    val deletedProductList = MutableLiveData<MutableList<ProductCategories>>()
    val products = MutableLiveData<GetProductCategoriesResponse>()
    val deleteIsSuccesfullyDone = MutableLiveData<Boolean>()

    fun fetchProductData() {
        if(connector.productCategoriesDao().getAllProductCategories().isEmpty()) {
            coroutineScope.launch {
                getProduct()
            }
        } else {
            progress.postValue(false)
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
                    connector.productCategoriesDao().insertProductCategories(ProductCategories(
                        response.productCategories[i].id,
                        response.productCategories[i].name,
                        response.productCategories[i].description,
                        response.productCategories[i].location_id
                    ))
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

    fun openCloseCheckBox() {
        when {
            deleteFab.value == null -> deleteFab.postValue(true)
            deleteFab.value!! -> deleteFab.postValue(false)
            else -> deleteFab.postValue(true)
        }
    }

    fun deleteMarkedProductCategories() {
        deleteConfirmFab.postValue(true)
    }

    fun delete() {
        coroutineScope.launch {
            deleteProduct()
        }
    }

    suspend fun deleteProduct() {
        progress.postValue(true)
        try {
            var problem = false
            deletedProductList.value!!.forEach {
                val response = sportAnalyticService.deleteProductCategories(it.id).awaitResponse().body()
                if (!response!!.status) {
                    problem = true
                }
            }
            if (!problem) {
                deletedProductList.value!!.forEach {
                    connector.productCategoriesDao().delete(it.id)
                }
                fetchProductData()
                deletedProductList.postValue(null)
                deleteFab.postValue(false)
                deleteConfirmFab.postValue(false)
                deleteIsSuccesfullyDone.postValue(true)
            } else {
                deleteIsSuccesfullyDone.postValue(false)
            }
        }catch (e: Exception) {
            error.postValue(e)
        }finally {
            progress.postValue(false)
        }
    }

    fun getAllNonDeletedProducts(): List<ProductCategories> {
        return connector.productCategoriesDao().getAllProductCategories()
    }
}

class ProductsEditFragmentViewModelFactory @Inject constructor(val connector: SportAnalyticDB,
                                                                val preferences: MyPreferences,
                                                                val sportAnalyticService: SportAnalyticService
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return DataEditProductCategoriesFragmentViewModel(connector, preferences, sportAnalyticService) as T
    }
}