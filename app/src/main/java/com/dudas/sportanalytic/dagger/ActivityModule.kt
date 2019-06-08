package com.dudas.sportanalytic.dagger

import android.content.Context
import com.dudas.sportanalytic.ui.BaseActivity
import dagger.Module
import dagger.Provides


@Module
class ActivityModule(private val activity: BaseActivity) {
    @Provides
    @ActivityContext
    fun provideActivityContext(): Context = activity
}