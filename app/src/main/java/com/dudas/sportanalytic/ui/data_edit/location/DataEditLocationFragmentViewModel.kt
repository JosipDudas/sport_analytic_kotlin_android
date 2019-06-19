package com.dudas.sportanalytic.ui.data_edit.location

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.dudas.sportanalytic.api.SportAnalyticService
import com.dudas.sportanalytic.api.response.GetLocationsResponse
import com.dudas.sportanalytic.database.SportAnalyticDB
import com.dudas.sportanalytic.database.entities.Location
import com.dudas.sportanalytic.preferences.MyPreferences
import com.dudas.sportanalytic.ui.BaseViewModel
import kotlinx.coroutines.launch
import ru.gildor.coroutines.retrofit.awaitResponse
import javax.inject.Inject

class DataEditLocationFragmentViewModel @Inject constructor(val connector: SportAnalyticDB,
                                                            val preferences: MyPreferences,
                                                            val sportAnalyticService: SportAnalyticService) : BaseViewModel() {
    val deleteFab = MutableLiveData<Boolean>()
    val deleteConfirmFab = MutableLiveData<Boolean>()
    val deletedLocationList = MutableLiveData<MutableList<Location>>()
    val locations = MutableLiveData<GetLocationsResponse>()

    fun fetchLocationData() {
        coroutineScope.launch {
            getLocation()
        }
    }

    suspend fun getLocation() {
        progress.postValue(true)
        try {
            val response = sportAnalyticService
                .getLocations(preferences.getUser()!!.companyId!!)
                .awaitResponse()
                .body()
            if (response!!.status) {
                for (i in 0 until response.locations!!.size) {
                    connector.locationDao().insertLocation(com.dudas.sportanalytic.database.entities.Location(
                        response.locations[i].id,
                        response.locations[i].name,
                        response.locations[i].description,
                        response.locations[i].company_id
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

    fun deleteMarkedLocations() {
        deleteConfirmFab.postValue(true)
    }

    fun delete(deleteList: MutableList<Location>) {
        deleteList.forEach {
            connector.locationDao().delete(it.id)
        }
        //TODO delete location on main DB
        deletedLocationList.postValue(null)
        deleteFab.postValue(false)
        deleteConfirmFab.postValue(false)
    }

    fun getAllNonDeletedLocations(): List<Location> {
        return connector.locationDao().getAllLocations()
    }
}

class LocationsEditFragmentViewModelFactory @Inject constructor(val connector: SportAnalyticDB,
                                                                val preferences: MyPreferences,
                                                                val sportAnalyticService: SportAnalyticService
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return DataEditLocationFragmentViewModel(connector, preferences, sportAnalyticService) as T
    }
}