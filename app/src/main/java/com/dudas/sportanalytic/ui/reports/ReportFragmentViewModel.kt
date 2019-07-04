package com.dudas.sportanalytic.ui.reports

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.dudas.sportanalytic.api.SportAnalyticService
import com.dudas.sportanalytic.database.SportAnalyticDB
import com.dudas.sportanalytic.database.entities.Product
import com.dudas.sportanalytic.database.entities.ProductCategories
import com.dudas.sportanalytic.database.entities.Report
import com.dudas.sportanalytic.preferences.MyPreferences
import com.dudas.sportanalytic.ui.BaseViewModel
import kotlinx.coroutines.launch
import ru.gildor.coroutines.retrofit.awaitResponse
import java.util.*
import javax.inject.Inject

class ReportFragmentViewModel @Inject constructor(val sportAnalyticService: SportAnalyticService,
                                                       val connector: SportAnalyticDB,
                                                       val preferences: MyPreferences
): BaseViewModel(){

    var report = MutableLiveData<Report>().default(
        Report(
            id = UUID.randomUUID().toString().toUpperCase(),
            date = null,
            location_id = preferences.getLocation()?.id!!)
    )
    var user = MutableLiveData<Boolean>().default(true)
    var productsList = MutableLiveData<List<ProductCategories>>()

    var popupWindowIsOpen = MutableLiveData<Boolean>()
    var products = MutableLiveData<List<Product>>()
    var selectedProducts = MutableLiveData<MutableList<Product>>()
    var popUpProgress = MutableLiveData<Boolean>()
    var errorMessage = MutableLiveData<Boolean>()
    var description = MutableLiveData<String>()

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
                    val productResponse = sportAnalyticService
                        .getProduct(response.productCategories[i].id)
                        .awaitResponse()
                        .body()
                    val allProductsInLocalDB = connector.productDao().getAllProducts()
                    if (productResponse!!.status) {
                        for (k in 0 until productResponse.product!!.size) {
                            var productExist = false
                            for (l in 0 until allProductsInLocalDB.size) {
                                if (allProductsInLocalDB[l].id == productResponse.product[k].id) {
                                    productExist = true
                                }
                            }
                            if (!productExist) {
                                connector.productDao().insertProduct(
                                    Product(
                                        productResponse.product[k].id,
                                        productResponse.product[k].name,
                                        productResponse.product[k].categorie_id
                                    ))
                            }
                        }
                    }
                    if (!exist) {
                        connector.productCategoriesDao().insertProductCategories(ProductCategories(
                            response.productCategories[i].id,
                            response.productCategories[i].name,
                            response.productCategories[i].description,
                            response.productCategories[i].location_id
                        ))
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

    fun getProductForCategory(productCategoryId: String){
        coroutineScope.launch {
            getProductsForCategoryId(productCategoryId)
        }
    }

    suspend fun getProductsForCategoryId(productCategoryId: String) {
        popUpProgress.postValue(true)
        try {
            val response = sportAnalyticService
                .getProduct(productCategoryId)
                .awaitResponse()
                .body()
            val allProductsInLocalDB = connector.productDao().getAllProducts()
            if (response!!.status) {
                for (i in 0 until response.product!!.size) {
                    var exist = false
                    for (j in 0 until allProductsInLocalDB.size) {
                        if (allProductsInLocalDB[j].id == response.product[i].id) {
                            exist = true
                        }
                    }
                    if (!exist) {
                        connector.productCategoriesDao().insertProductCategories(ProductCategories(
                            response.product[i].id,
                            response.product[i].name,
                            response.product[i].categorie_id
                        ))
                        exist = false
                    }
                }
            }
            products.postValue(connector.productDao().getSpecificProducts(productCategoryId))
        }catch (e: Exception) {
            error.postValue(e)
        } finally {
            popUpProgress.postValue(false)
        }
    }

    fun addToProductList(product: Product) {
        if (selectedProducts.value == null) {
            selectedProducts.value = mutableListOf()
            selectedProducts.value!!.add(product)
        }
        var exist = false
        for (i in 0 until selectedProducts.value!!.size) {
            if (selectedProducts.value!![i].id == product.id) {
                exist = true
            }
        }
        if (!exist) {
            selectedProducts.value!!.add(product)
        }
    }

    fun removeProductFromList(product: Product) {
        selectedProducts.value!!.remove(product)
    }
}

class ReportFragmentViewModelFactory @Inject constructor(val sportAnalyticService: SportAnalyticService,
                                                              val connector: SportAnalyticDB,
                                                              val preferences: MyPreferences
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return ReportFragmentViewModel(sportAnalyticService, connector, preferences) as T
    }
}