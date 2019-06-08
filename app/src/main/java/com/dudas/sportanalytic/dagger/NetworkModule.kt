package com.dudas.sportanalytic.dagger

import com.dudas.sportanalytic.BuildConfig
import com.dudas.sportanalytic.api.SportAnalyticService
import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton


@Module
open class NetworkModule() {
    @Provides
    @BaseUrl
    fun provideBaseUrl() = "https://sport-analytic.000webhostapp.com"

    @Provides
    @Singleton
    fun provideHttpClient(): OkHttpClient {
        val TIMEOUT_SECONDS = 30L
        return OkHttpClient().newBuilder()
                .callTimeout(TIMEOUT_SECONDS, TimeUnit.SECONDS)
                .connectTimeout(TIMEOUT_SECONDS, TimeUnit.SECONDS)
                .readTimeout(TIMEOUT_SECONDS, TimeUnit.SECONDS)
                .addInterceptor(HttpLoggingInterceptor().apply {
                    level = if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.BASIC
                })
                .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient, gson: Gson, @BaseUrl baseUrl: String): Retrofit {
        return Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build()
    }

    @Provides
    @Singleton
    open fun provideSportAnalyticService(retrofit: Retrofit) = retrofit.create(SportAnalyticService::class.java)
}