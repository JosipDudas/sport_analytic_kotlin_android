package com.dudas.sportanalytic.ui.registration

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.dudas.sportanalytic.api.SportAnalyticService
import com.dudas.sportanalytic.database.SportAnalyticDB
import com.dudas.sportanalytic.preferences.MyPreferences
import com.dudas.sportanalytic.ui.BaseViewModel
import com.dudas.sportanalytic.utils.generateHashOfPassword
import kotlinx.coroutines.launch
import ru.gildor.coroutines.retrofit.awaitResponse
import java.util.*
import javax.inject.Inject

class RegistrationViewModel @Inject constructor(val preferences: MyPreferences,
                                         val sportAnalyticService: SportAnalyticService,
                                         val connector: SportAnalyticDB
) : BaseViewModel() {

    val firstname = MutableLiveData<String>()
    val lastname = MutableLiveData<String>()
    val password = MutableLiveData<String>()
    val email = MutableLiveData<String>()
    val position = MutableLiveData<String>()
    val address = MutableLiveData<String>()
    val sex = MutableLiveData<String>()
    val enableRegisterButton = MutableLiveData<Boolean>()
    val registerIsSuccess = MutableLiveData<Boolean>()

    fun isFormValid(){
        enableRegisterButton.postValue(!firstname.value.isNullOrEmpty() && !lastname.value.isNullOrEmpty()
                && !password.value.isNullOrEmpty() && !email.value.isNullOrEmpty()
                && !position.value.isNullOrEmpty() && !address.value.isNullOrEmpty()
                && !sex.value.isNullOrEmpty() && isEmailValid(email.value!!))
    }

    private fun isEmailValid(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    fun positionIntToString(positionInt: Int){
        when(positionInt) {
            1 -> position.postValue("admin")
            else -> position.postValue("user")
        }
        isFormValid()
    }

    fun sexIntToString(sexInt: Int){
        when(sexInt) {
            1 -> sex.postValue("M")
            else -> sex.postValue("F")
        }
        isFormValid()
    }

    fun userRegistration() {
        coroutineScope.launch {
            register()
        }
    }

    suspend fun register() {
        progress.postValue(true)
        try {
            val response = sportAnalyticService
                .createUser(UUID.randomUUID().toString().toUpperCase(),
                    firstname.value!!,
                    lastname.value!!,
                    generateHashOfPassword(password.value!!),
                    email.value!!,
                    position.value!!,
                    address.value!!,
                    sex.value!!)
                .awaitResponse()
                .body()
            if (response!!.status) {
                registerIsSuccess.postValue(true)
            } else {
                error.postValue(IllegalStateException(response.message))
                registerIsSuccess.postValue(false)
            }
        }catch (e: Exception) {
            error.postValue(e)
        }finally {
            progress.postValue(false)
        }
    }

}

class RegistrationViewModelFactory @Inject constructor(val preferences: MyPreferences,
                                                val sportAnalyticService: SportAnalyticService,
                                                val connector: SportAnalyticDB
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return RegistrationViewModel(preferences, sportAnalyticService, connector) as T
    }
}