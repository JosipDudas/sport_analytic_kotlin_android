package com.dudas.sportanalytic.ui.main

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.dudas.sportanalytic.api.SportAnalyticService
import com.dudas.sportanalytic.database.SportAnalyticDB
import com.dudas.sportanalytic.preferences.MyPreferences
import com.dudas.sportanalytic.ui.BaseViewModel
import javax.inject.Inject

class MainActivityViewModel @Inject constructor(val preferences: MyPreferences,
                                         val sportAnalyticService: SportAnalyticService,
                                         val connector: SportAnalyticDB
) : BaseViewModel() {

    val locationMessage = MutableLiveData<Boolean>()

    fun checkIfUserChooseLocation() {
        if (preferences.getUser()!!.position != "admin") {
            if (preferences.getLocation() == null) {
                locationMessage.postValue(true)
            } else {
                locationMessage.postValue(false)
            }
        }
    }
}

class MainActivityViewModelFactory @Inject constructor(val preferences: MyPreferences,
                                                val sportAnalyticService: SportAnalyticService,
                                                val connector: SportAnalyticDB
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return MainActivityViewModel(preferences, sportAnalyticService, connector) as T
    }
}