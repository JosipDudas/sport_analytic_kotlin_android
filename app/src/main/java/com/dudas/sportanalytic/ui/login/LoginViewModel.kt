package com.dudas.sportanalytic.ui.login

import androidx.core.util.PatternsCompat
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.dudas.sportanalytic.api.SportAnalyticService
import com.dudas.sportanalytic.database.SportAnalyticDB
import com.dudas.sportanalytic.database.entities.User
import com.dudas.sportanalytic.preferences.LoggedInUser
import com.dudas.sportanalytic.preferences.MyPreferences
import com.dudas.sportanalytic.ui.BaseViewModel
import com.dudas.sportanalytic.utils.generateHashOfPassword
import kotlinx.coroutines.launch
import ru.gildor.coroutines.retrofit.awaitResponse
import javax.inject.Inject


class LoginViewModel @Inject constructor(val preferences: MyPreferences,
                                         val sportAnalyticService: SportAnalyticService,
                                         val connector: SportAnalyticDB) : BaseViewModel() {

    val email = MutableLiveData<String>()
    val password = MutableLiveData<String>()
    val enableLoginButton = MutableLiveData<Boolean>()
    val loginIsSuccess = MutableLiveData<Boolean>()
    val startRegistrationActivity = MutableLiveData<Boolean>()

    fun isFormValid(){
        enableLoginButton.postValue(!email.value.isNullOrEmpty() && !password.value.isNullOrEmpty()
                && isEmailValid(email.value!!))
    }

    private fun isEmailValid(email: String): Boolean {
        return PatternsCompat.EMAIL_ADDRESS.matcher(email).matches()
    }

    fun onLoginClick() {
        coroutineScope.launch {
            login()
        }
    }

    suspend fun login() {
        progress.postValue(true)
        try {
            val response = sportAnalyticService
                .userLogin(email.value!!, generateHashOfPassword(password.value!!))
                .awaitResponse()
                .body()
            if (response!!.status) {
                loginIsSuccess.postValue(true)
                preferences.setUser(
                    LoggedInUser(response.id,
                        response.firstname,
                        response.lastname,
                        generateHashOfPassword(response.password),
                        response.email,
                        response.position,
                        response.address,
                        response.sex,
                        response.company_id))
                connector.userDao().insertUser(User(response.id,
                    response.firstname,
                    response.lastname,
                    generateHashOfPassword(response.password),
                    response.email,
                    response.position,
                    response.address,
                    response.sex,
                    response.company_id))
            } else {
                error.postValue(IllegalStateException(response.message))
                loginIsSuccess.postValue(false)
            }
        }catch (e: Exception) {
            error.postValue(e)
        }finally {
            progress.postValue(false)
        }
    }

    fun onRegisterClick() {
        startRegistrationActivity.postValue(true)
    }
}

class LoginViewModelFactory @Inject constructor(val preferences: MyPreferences,
                                                val sportAnalyticService: SportAnalyticService,
                                                 val connector: SportAnalyticDB) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return LoginViewModel(preferences, sportAnalyticService, connector) as T
    }
}