package com.dudas.sportanalytic.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.dudas.sportanalytic.constants.DBConstants
import com.dudas.sportanalytic.database.dao.LocationDao
import com.dudas.sportanalytic.database.dao.ProductCategoriesDao
import com.dudas.sportanalytic.database.dao.ProductDao
import com.dudas.sportanalytic.database.dao.UserDao
import com.dudas.sportanalytic.database.entities.Location
import com.dudas.sportanalytic.database.entities.Product
import com.dudas.sportanalytic.database.entities.ProductCategories
import com.dudas.sportanalytic.database.entities.User

@Database(entities = [(User::class),(Location::class),(Product::class),(ProductCategories::class)],
    version = DBConstants.DATABASE_VERSION
)
abstract class SportAnalyticDB : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun locationDao(): LocationDao
    abstract fun productDao(): ProductDao
    abstract fun productCategoriesDao(): ProductCategoriesDao
}