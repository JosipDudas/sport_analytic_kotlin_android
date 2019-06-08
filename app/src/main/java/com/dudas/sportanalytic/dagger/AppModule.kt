package com.dudas.sportanalytic.dagger

import android.content.Context
import androidx.room.Room
import com.dudas.sportanalytic.BuildConfig
import com.dudas.sportanalytic.constants.DBConstants
import com.dudas.sportanalytic.database.SportAnalyticDB
import com.dudas.sportanalytic.preferences.MyPreferences
import dagger.Module
import dagger.Provides
import net.orange_box.storebox.StoreBox
import javax.inject.Singleton


@Module
class AppModule(@AppContext private val context: Context) {

    @Provides
    @Singleton
    fun providePreferences() = StoreBox.create(context, MyPreferences::class.java)

    @Provides
    @Singleton
    fun provideDatabase(@DatabaseInfo name: String): SportAnalyticDB {
        val builder = Room.databaseBuilder(
                context,
                SportAnalyticDB::class.java,
                name
        ).allowMainThreadQueries()
        return if (BuildConfig.DEBUG) builder.fallbackToDestructiveMigration().build() else builder.build()
    }

    @Provides
    @Singleton
    @DatabaseInfo
    fun provideDatabaseName() = DBConstants.DATABASE_NAME
}