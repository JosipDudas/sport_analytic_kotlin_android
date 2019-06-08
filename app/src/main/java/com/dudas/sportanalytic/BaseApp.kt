package com.dudas.sportanalytic

import android.app.Application
import com.dudas.sportanalytic.dagger.AppComponent
import com.dudas.sportanalytic.dagger.DaggerAppComponent
import timber.log.Timber


abstract class BaseApp : Application() {
    val app: AppComponent by lazy {
        DaggerAppComponent
            .builder()
            .build()
    }

    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
        app.inject(this)
    }
}

fun Application.getComponent(): AppComponent = (this as BaseApp).app