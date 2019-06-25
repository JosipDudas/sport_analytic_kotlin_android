package com.dudas.sportanalytic.utils

import android.annotation.SuppressLint
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.*

@SuppressLint("SimpleDateFormat")
fun getDateFormatForReservationDate(date: Date): String {
    val formatter = SimpleDateFormat("yyyy-MM-dd")
    return formatter.format(date)
}

fun fromReservationDateToDate(date: String): Date {
    val calendar = Calendar.getInstance()
    calendar.set(Calendar.YEAR, date.take(4).toInt())
    calendar.set(Calendar.MONTH, date.substring(4,6).toInt())
    calendar.set(Calendar.DAY_OF_MONTH, date.takeLast(2).toInt())
    return calendar.time
}