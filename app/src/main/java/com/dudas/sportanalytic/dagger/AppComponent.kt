package com.dudas.sportanalytic.dagger

import com.dudas.sportanalytic.BaseApp
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = arrayOf(AppModule::class, NetworkModule::class))
interface AppComponent{
    fun inject(app : BaseApp)
}