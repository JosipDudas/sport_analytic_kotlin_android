package com.dudas.sportanalytic.ui

class BaseDrawerActivityViewModel (private val callback: CallBack) {

    fun backDrawerChecker(drawer: Boolean) {
        if (drawer) {
            callback.closeDrawer()
        } else {
            callback.closeApp()
        }
    }

    interface CallBack {
        fun closeDrawer()
        fun closeApp()
    }
}