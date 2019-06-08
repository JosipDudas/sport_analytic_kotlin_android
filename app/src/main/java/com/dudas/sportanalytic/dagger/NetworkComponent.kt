package com.dudas.sportanalytic.dagger

import com.dudas.sportanalytic.ui.login.LoginViewModel
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = arrayOf(NetworkModule::class, CommonModule::class, AppModule::class))
interface NetworkComponent{
    fun inject(loginViewModel: LoginViewModel)
}