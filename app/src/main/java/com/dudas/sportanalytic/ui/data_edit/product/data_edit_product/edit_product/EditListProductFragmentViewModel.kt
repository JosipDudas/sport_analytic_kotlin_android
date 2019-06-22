package com.dudas.sportanalytic.ui.data_edit.product.data_edit_product.edit_product

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.dudas.sportanalytic.api.SportAnalyticService
import com.dudas.sportanalytic.api.response.GetProductResponse
import com.dudas.sportanalytic.database.SportAnalyticDB
import com.dudas.sportanalytic.database.entities.Product
import com.dudas.sportanalytic.preferences.MyPreferences
import com.dudas.sportanalytic.ui.BaseViewModel
import kotlinx.coroutines.launch
import ru.gildor.coroutines.retrofit.awaitResponse
import javax.inject.Inject

class EditListProductFragmentViewModel @Inject constructor(val connector: SportAnalyticDB,
    val preferences: MyPreferences,
    val sportAnalyticService: SportAnalyticService) : BaseViewModel() {
    val deleteFab = MutableLiveData<Boolean>()
    val deleteConfirmFab = MutableLiveData<Boolean>()
    val deletedProductList = MutableLiveData<MutableList<Product>>()
    val products = MutableLiveData<GetProductResponse>()
    val deleteIsSuccesfullyDone = MutableLiveData<Boolean>()
    val categoryId = MutableLiveData<String>()

        fun fetchProductData() {
            if(connector.productDao().getAllProducts().isEmpty()) {
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
                    .getProduct(categoryId.value!!)
                    .awaitResponse()
                    .body()
                if (response!!.status) {
                    for (i in 0 until response.product!!.size) {
                        connector.productDao().insertProduct(
                            Product(
                                response.product[i].id,
                                response.product[i].name,
                                response.product[i].categorie_id
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

        fun openCloseCheckBox() {
            when {
                deleteFab.value == null -> deleteFab.postValue(true)
                deleteFab.value!! -> deleteFab.postValue(false)
                else -> deleteFab.postValue(true)
            }
        }

        fun deleteMarkedProduct() {
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
                    val response = sportAnalyticService.deleteProduct(it.id).awaitResponse().body()
                    if (!response!!.status) {
                        problem = true
                    }
                }
                if (!problem) {
                    deletedProductList.value!!.forEach {
                        connector.productDao().delete(it.id)
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

        fun getAllNonDeletedProducts(): List<Product> {
            return connector.productDao().getAllProducts()
        }
}

class EditListProductFragmentViewModelFactory @Inject constructor(val connector: SportAnalyticDB,
                                                               val preferences: MyPreferences,
                                                               val sportAnalyticService: SportAnalyticService
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return EditListProductFragmentViewModel(connector, preferences, sportAnalyticService) as T
    }
}