package com.dudas.sportanalytic.ui.main

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

class MainFragmentViewModel @Inject constructor(val preferences: MyPreferences,
                                                    val connector: SportAnalyticDB,
                                                    val sportAnalyticService: SportAnalyticService
): BaseViewModel() {


}

class MainFragmentViewModelFactory @Inject constructor(val preferences: MyPreferences,
                                                           val connector: SportAnalyticDB,
                                                           val sportAnalyticService: SportAnalyticService
):
    ViewModelProvider.Factory{
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return MainFragmentViewModel(preferences, connector, sportAnalyticService) as T
    }
}