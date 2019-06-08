package com.dudas.sportanalytic

import com.facebook.stetho.Stetho

class App : BaseApp() {
    override fun onCreate() {
        super.onCreate()
        Stetho.initializeWithDefaults(this)
    }
}