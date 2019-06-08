package com.dudas.sportanalytic.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job

abstract class BaseViewModel : ViewModel() {
    val job = Job()
    val coroutineScope = CoroutineScope(Dispatchers.Default + job)

    val progress = MutableLiveData<Boolean>()
    val error = MutableLiveData<Exception>()

    override fun onCleared() {
        super.onCleared()
        job.cancel()
    }
}