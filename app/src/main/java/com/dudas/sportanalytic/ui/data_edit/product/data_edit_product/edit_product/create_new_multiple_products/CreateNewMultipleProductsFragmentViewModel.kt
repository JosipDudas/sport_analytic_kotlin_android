package com.dudas.sportanalytic.ui.data_edit.product.data_edit_product.edit_product.create_new_multiple_products

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
import java.util.*
import javax.inject.Inject

class CreateNewMultipleProductsFragmentViewModel @Inject constructor(val sportAnalyticService: SportAnalyticService,
                                                                     val preferences: MyPreferences,
                                                                     val connector: SportAnalyticDB
) : BaseViewModel() {

    val from =  MutableLiveData<String>()
    val to =  MutableLiveData<String>()
    val productName =  MutableLiveData<String>()
    val product = MutableLiveData<Product>()
    val editFragment = MutableLiveData<Boolean>()
    val enableInsertUpdateButton = MutableLiveData<Boolean>()
    val closeFragment = MutableLiveData<Boolean>()

    fun newProduct(categoryId: String) {
        editFragment.value = false
        product.value = Product(
            id = UUID.randomUUID().toString().toUpperCase(),
            name = "",
            categorie_id = categoryId)
    }

    fun onInsertUpdateClick(){
        coroutineScope.launch {
            insertProduct()
        }
    }

    suspend fun insertProduct() {
        progress.postValue(true)
        try {
            for (i in from.value!!.toInt() until (to.value!!.toInt()+1)) {
                product.value?.id = UUID.randomUUID().toString().toUpperCase()
                product.value?.name = productName.value!!.toString()  + i.toString()
                val response = sportAnalyticService.insertProduct(product.value!!.id,
                    product.value!!.name!!,
                    product.value!!.categorie_id!!)
                    .awaitResponse()
                    .body()
                if (response!!.status) {
                    connector.productDao().insertProduct(
                        Product(product.value!!.id,
                            product.value!!.name,
                            product.value!!.categorie_id)
                    )
                }else {
                    error.postValue(IllegalStateException(response.message))
                    break
                }
            }
        }catch (e: Exception) {
            error.postValue(e)
        }finally {
            progress.postValue(false)
        }
    }

    fun isFormValid(){
        if (productName.value != null && from.value != null && to.value != null){
            enableInsertUpdateButton.value = !productName.value!!.isEmpty() && !from.value!!.isEmpty() && !to.value!!.isEmpty()
        } else {
            enableInsertUpdateButton.value = false
        }
    }

}
class CreateNewMultipleProductsFragmentViewModelFactory @Inject constructor(val sportAnalyticService: SportAnalyticService,
                                                                   val preferences: MyPreferences,
                                                                   val connector: SportAnalyticDB
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return CreateNewMultipleProductsFragmentViewModel(sportAnalyticService, preferences, connector) as T
    }
}