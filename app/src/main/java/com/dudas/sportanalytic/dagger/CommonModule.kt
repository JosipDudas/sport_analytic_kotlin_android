package com.dudas.sportanalytic.dagger

import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import org.greenrobot.eventbus.EventBus
import javax.inject.Singleton

@Module
class CommonModule {
    @Provides
    @Singleton
    fun provideGson() = GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").create()
    
    @Provides
    @Singleton
    fun provideEventBus(): EventBus = EventBus.getDefault()
}