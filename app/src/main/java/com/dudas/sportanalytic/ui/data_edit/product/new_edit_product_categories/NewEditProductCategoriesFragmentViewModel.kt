package com.dudas.sportanalytic.ui.data_edit.product.new_edit_product_categories

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.dudas.sportanalytic.api.SportAnalyticService
import com.dudas.sportanalytic.database.SportAnalyticDB
import com.dudas.sportanalytic.database.entities.ProductCategories
import com.dudas.sportanalytic.preferences.MyPreferences
import com.dudas.sportanalytic.ui.BaseViewModel
import kotlinx.coroutines.launch
import ru.gildor.coroutines.retrofit.awaitResponse
import java.util.*
import javax.inject.Inject

class NewEditProductCategoriesFragmentViewModel @Inject constructor(val sportAnalyticService: SportAnalyticService,
                                                           val preferences: MyPreferences,
                                                           val connector: SportAnalyticDB
) : BaseViewModel() {

    val productCategoriesName =  MutableLiveData<String>()
    val productCategoriesDescription =  MutableLiveData<String>()
    val onChangeOrientation = MutableLiveData<Boolean>()
    val productCategories = MutableLiveData<ProductCategories>()
    val editFragment = MutableLiveData<Boolean>()
    val enableInsertUpdateButton = MutableLiveData<Boolean>()
    val closeFragment = MutableLiveData<Boolean>()

    fun editProductCategories(productCategoriesId: String){
        editFragment.value = true
        productCategories.value = connector.productCategoriesDao().getSpecificProductCategories(productCategoriesId)

        productCategoriesName.value = productCategories.value?.name
        productCategoriesDescription.value = productCategories.value?.description
    }

    fun newProductCategories() {
        editFragment.value = false
        productCategories.value = ProductCategories(
            id = UUID.randomUUID().toString().toUpperCase(),
            name = "",
            description = "",
            location_id = preferences.getLocation()!!.id)
    }

    fun onInsertUpdateClick(){
        productCategories.value?.name = productCategoriesName.value!!.toString()
        productCategories.value?.location_id = preferences.getLocation()!!.id
        productCategories.value?.description = productCategoriesDescription.value!!.toString()
        if (editFragment.value!!) {
            coroutineScope.launch {
                updateProductCategories()
            }
        } else {
            coroutineScope.launch {
                insertProductCategories()
            }
        }
    }

    suspend fun updateProductCategories() {
        progress.postValue(true)
        try {
            val response = sportAnalyticService.updateProductCategories(productCategories.value!!.id,
                productCategories.value!!.name!!,
                productCategories.value!!.description!!,
                productCategories.value!!.location_id!!).awaitResponse().body()
            if (response!!.status) {
                connector.productCategoriesDao().updateProductCategories(ProductCategories(productCategories.value!!.id,
                    productCategories.value!!.name,
                    productCategories.value!!.description,
                    productCategories.value!!.location_id))
            }else {
                error.postValue(IllegalStateException(response.message))
            }
        }catch (e: Exception) {
            error.postValue(e)
        }finally {
            progress.postValue(false)
        }
    }

    suspend fun insertProductCategories() {
        progress.postValue(true)
        try {
            val response = sportAnalyticService.insertProductCategories(productCategories.value!!.id,
                productCategories.value!!.name!!,
                productCategories.value!!.description!!,
                productCategories.value!!.location_id!!).awaitResponse().body()
            if (response!!.status) {
                connector.productCategoriesDao().insertProductCategories(ProductCategories(productCategories.value!!.id,
                    productCategories.value!!.name,
                    productCategories.value!!.description,
                    productCategories.value!!.location_id))
            }else {
                error.postValue(IllegalStateException(response.message))
            }
        }catch (e: Exception) {
            error.postValue(e)
        }finally {
            progress.postValue(false)
        }
    }

    fun isFormValid(){
        if (productCategoriesName.value != null && productCategoriesDescription.value != null){
            enableInsertUpdateButton.value = !productCategoriesName.value!!.isEmpty() && !productCategoriesDescription.value!!.isEmpty()
        } else {
            enableInsertUpdateButton.value = false
        }
    }

}
class NewEditProductCategoriesFragmentViewModelFactory @Inject constructor(val sportAnalyticService: SportAnalyticService,
                                                                  val preferences: MyPreferences,
                                                                  val connector: SportAnalyticDB
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return NewEditProductCategoriesFragmentViewModel(sportAnalyticService, preferences, connector) as T
    }
}