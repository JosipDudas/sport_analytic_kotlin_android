package com.dudas.sportanalytic.dagger

import com.dudas.sportanalytic.ui.BaseActivity
import com.dudas.sportanalytic.ui.BaseFragment
import com.dudas.sportanalytic.ui.login.LoginActivity
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = arrayOf(ActivityModule::class, AppModule::class, CommonModule::class, NetworkModule::class))
interface ActivityComponent{
    fun inject(baseFragment: BaseFragment)
    fun inject(activity: LoginActivity)
    fun inject(baseActivity: BaseActivity)
}