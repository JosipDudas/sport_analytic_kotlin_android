package com.dudas.sportanalytic.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.dudas.sportanalytic.constants.DBConstants
import com.dudas.sportanalytic.database.dao.*
import com.dudas.sportanalytic.database.entities.*

@Database(entities = [(User::class),(Location::class),(Product::class),(ProductCategories::class),
    (Reservation::class),(ReservationItem::class),(Report::class),(ReportItem::class)],
    version = DBConstants.DATABASE_VERSION
)
abstract class SportAnalyticDB : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun locationDao(): LocationDao
    abstract fun productDao(): ProductDao
    abstract fun productCategoriesDao(): ProductCategoriesDao
    abstract fun reservationDao(): ReservationDao
    abstract fun reservationItemDao(): ReservationItemDao
    abstract fun reportDao(): ReportDao
    abstract fun reportItemDao(): ReportItemDao
}