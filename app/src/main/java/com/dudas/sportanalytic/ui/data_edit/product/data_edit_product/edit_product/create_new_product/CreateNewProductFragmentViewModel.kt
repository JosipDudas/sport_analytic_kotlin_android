package com.dudas.sportanalytic.ui.data_edit.product.data_edit_product.edit_product.create_new_product

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

class CreateNewProductFragmentViewModel @Inject constructor(val sportAnalyticService: SportAnalyticService,
                                                            val preferences: MyPreferences,
                                                            val connector: SportAnalyticDB
) : BaseViewModel() {

    val productName =  MutableLiveData<String>()
    val onChangeOrientation = MutableLiveData<Boolean>()
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
        product.value?.name = productName.value!!.toString()
        coroutineScope.launch {
            insertProduct()
        }
    }

    suspend fun insertProduct() {
        progress.postValue(true)
        try {
            val response = sportAnalyticService.insertProduct(product.value!!.id,
                product.value!!.name!!,
                product.value!!.categorie_id!!).awaitResponse().body()
            if (response!!.status) {
                connector.productDao().insertProduct(
                    Product(product.value!!.id,
                        product.value!!.name,
                        product.value!!.categorie_id)
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
class CreateNewProductFragmentViewModelFactory @Inject constructor(val sportAnalyticService: SportAnalyticService,
                                                                           val preferences: MyPreferences,
                                                                           val connector: SportAnalyticDB
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return CreateNewProductFragmentViewModel(sportAnalyticService, preferences, connector) as T
    }
}