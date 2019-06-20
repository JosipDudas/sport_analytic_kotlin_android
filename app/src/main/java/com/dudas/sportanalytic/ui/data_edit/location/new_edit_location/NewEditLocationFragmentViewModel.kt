package com.dudas.sportanalytic.ui.data_edit.location.new_edit_location

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.dudas.sportanalytic.api.SportAnalyticService
import com.dudas.sportanalytic.api.response.GetCompaniesResponse
import com.dudas.sportanalytic.database.SportAnalyticDB
import com.dudas.sportanalytic.database.entities.Location
import com.dudas.sportanalytic.preferences.MyPreferences
import com.dudas.sportanalytic.ui.BaseViewModel
import kotlinx.coroutines.launch
import ru.gildor.coroutines.retrofit.awaitResponse
import java.util.*
import javax.inject.Inject

class NewEditLocationFragmentViewModel @Inject constructor(val sportAnalyticService: SportAnalyticService,
                                                            val preferences: MyPreferences,
                                                            val connector: SportAnalyticDB) : BaseViewModel() {

    val locationName =  MutableLiveData<String>()
    val locationCompanyId =  MutableLiveData<String>()
    val locationDescription =  MutableLiveData<String>()
    val onChangeOrientation = MutableLiveData<Boolean>()
    val location = MutableLiveData<Location>()
    val editFragment = MutableLiveData<Boolean>()
    val enableInsertUpdateButton = MutableLiveData<Boolean>()
    val closeFragment = MutableLiveData<Boolean>()

    fun editLocation(locationId: String){
        editFragment.value = true
        location.value = connector.locationDao().getSpecificLocation(locationId)

        locationName.value = location.value?.name
        locationDescription.value = location.value?.description
        locationCompanyId.value = preferences.getUser()!!.companyId
    }

    fun newLocation() {
        editFragment.value = false
        location.value = Location(
            id = UUID.randomUUID().toString().toUpperCase(),
            name = "",
            description = "",
            company_id = preferences.getUser()!!.companyId)
    }

    fun onInsertUpdateClick(){
        location.value?.name = locationName.value!!.toString()
        location.value?.company_id = preferences.getUser()!!.companyId
        location.value?.description = locationDescription.value!!.toString()
        if (editFragment.value!!) {
            coroutineScope.launch {
                updateLocation()
            }
        } else {
            coroutineScope.launch {
                insertLocation()
            }
        }
    }

    suspend fun updateLocation() {
        progress.postValue(true)
        try {
            val response = sportAnalyticService.updateLocations(location.value!!.id,
                location.value!!.name!!,
                location.value!!.description!!,
                location.value!!.company_id!!).awaitResponse().body()
            if (response!!.status) {
                connector.locationDao().updateLocation(location.value!!)
            }else {
                error.postValue(IllegalStateException(response.message))
            }
        }catch (e: Exception) {
            error.postValue(e)
        }finally {
            progress.postValue(false)
        }
    }

    suspend fun insertLocation() {
        progress.postValue(true)
        try {
            val response = sportAnalyticService.insertLocations(location.value!!.id,
                location.value!!.name!!,
                location.value!!.description!!,
                location.value!!.company_id!!).awaitResponse().body()
            if (response!!.status) {
                connector.locationDao().insertLocation(location.value!!)
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
        if (locationName.value != null && locationDescription.value != null){
            enableInsertUpdateButton.value = !locationName.value!!.isEmpty() && !locationDescription.value!!.isEmpty()
        } else {
            enableInsertUpdateButton.value = false
        }
    }

}
class NewEditLocationFragmentViewModelFactory @Inject constructor(val sportAnalyticService: SportAnalyticService,
                                                                  val preferences: MyPreferences,
                                                                  val connector: SportAnalyticDB
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return NewEditLocationFragmentViewModel(sportAnalyticService, preferences, connector) as T
    }
}