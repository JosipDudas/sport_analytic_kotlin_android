package com.dudas.sportanalytic.ui.location

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.dudas.sportanalytic.api.SportAnalyticService
import com.dudas.sportanalytic.api.response.GetLocationsResponse
import com.dudas.sportanalytic.api.response.Locations
import com.dudas.sportanalytic.database.SportAnalyticDB
import com.dudas.sportanalytic.preferences.Location
import com.dudas.sportanalytic.preferences.MyPreferences
import com.dudas.sportanalytic.ui.BaseViewModel
import kotlinx.coroutines.launch
import ru.gildor.coroutines.retrofit.awaitResponse
import javax.inject.Inject

class LocationFragmentViewModel @Inject constructor(val preferences: MyPreferences,
                                                    val connector: SportAnalyticDB,
                                                    val sportAnalyticService: SportAnalyticService): BaseViewModel() {

    val locations = MutableLiveData<GetLocationsResponse>()
    val location = MutableLiveData<Locations>()
    val saveButton = MutableLiveData<Boolean>()

    fun getAllLocation() {
        coroutineScope.launch {
            getLocation()
        }
    }

    suspend fun getLocation() {
        progress.postValue(true)
        saveButton.postValue(false)
        try {
            val response = sportAnalyticService
                .getLocations(preferences.getUser()!!.companyId!!)
                .awaitResponse()
                .body()
            if (response!!.status) {
                locations.postValue(response)
                location.postValue(locations.value!!.locations!![0])
            } else {
                error.postValue(IllegalStateException(response.message))
            }
        }catch (e: Exception) {
            error.postValue(e)
        }finally {
            progress.postValue(false)
        }
    }

    fun locationSave() {
        preferences.setLocation(
            Location(location.value!!.id,
                location.value!!.name,
                location.value!!.description,
                location.value!!.company_id))
        saveButton.postValue(true)
    }

    fun saveLocation(position: Int) {
        for (i in 0 until locations.value!!.locations!!.size) {
            if (position == i ) {
                location.postValue(Locations(locations.value!!.locations!![i].id,
                    locations.value!!.locations!![i].name,
                    locations.value!!.locations!![i].description,
                    locations.value!!.locations!![i].company_id))
            }
        }
    }
}

class LocationFragmentViewModelFactory @Inject constructor(val preferences: MyPreferences,
                                                           val connector: SportAnalyticDB,
                                                           val sportAnalyticService: SportAnalyticService):
    ViewModelProvider.Factory{
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return LocationFragmentViewModel(preferences, connector, sportAnalyticService) as T
    }
}