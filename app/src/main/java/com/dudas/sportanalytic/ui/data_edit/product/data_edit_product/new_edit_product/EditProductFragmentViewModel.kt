package com.dudas.sportanalytic.ui.data_edit.product.data_edit_product.new_edit_product

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.dudas.sportanalytic.api.SportAnalyticService
import com.dudas.sportanalytic.database.SportAnalyticDB
import com.dudas.sportanalytic.database.entities.Product
import com.dudas.sportanalytic.preferences.MyPreferences
import com.dudas.sportanalytic.ui.BaseViewModel
import kotlinx.coroutines.launch
import ru.gildor.coroutines.retrofit.awaitResponse
import javax.inject.Inject

class EditProductFragmentViewModel @Inject constructor(val sportAnalyticService: SportAnalyticService,
                                                      val preferences: MyPreferences,
                                                      val connector: SportAnalyticDB
) : BaseViewModel() {

    val productName =  MutableLiveData<String>()
    val onChangeOrientation = MutableLiveData<Boolean>()
    val products = MutableLiveData<Product>()
    val editFragment = MutableLiveData<Boolean>()
    val enableInsertUpdateButton = MutableLiveData<Boolean>()
    val closeFragment = MutableLiveData<Boolean>()
    val productId = MutableLiveData<String>()

    fun editProduct(productsId: String){
        editFragment.value = true
        products.value = connector.productDao().getSpecificProduct(productsId)

        productName.value = products.value?.name
    }

    fun onInsertUpdateClick(){
        products.value?.name = productName.value!!.toString()
        coroutineScope.launch {
            updateProduct()
        }
    }

    suspend fun updateProduct() {
        progress.postValue(true)
        try {
            val response = sportAnalyticService.updateProduct(products.value!!.id,
                products.value!!.name!!,
                products.value!!.categorie_id!!).awaitResponse().body()
            if (response!!.status) {
                connector.productDao().updateProduct(
                    Product(products.value!!.id,
                        products.value!!.name,
                        products.value!!.categorie_id)
                )
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
        if (productName.value != null){
            enableInsertUpdateButton.value = !productName.value!!.isEmpty()
        } else {
            enableInsertUpdateButton.value = false
        }
    }

}
class EditProductFragmentViewModelFactory @Inject constructor(val sportAnalyticService: SportAnalyticService,
                                                                           val preferences: MyPreferences,
                                                                           val connector: SportAnalyticDB
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return EditProductFragmentViewModel(sportAnalyticService, preferences, connector) as T
    }
}