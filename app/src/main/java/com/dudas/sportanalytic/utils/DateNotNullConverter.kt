package com.dudas.sportanalytic.utils

import androidx.room.TypeConverter
import java.sql.Date


class DateNotNullConverter {
    @TypeConverter
    fun fromTimestamp(value: Long): Date {
        return Date(value)
    }

    @TypeConverter
    fun dateToTimestamp(date: Date): Long {
        return date.time
    }
}