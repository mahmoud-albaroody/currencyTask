package com.bitaqaty.currencyapp.utils

import java.text.SimpleDateFormat
import java.util.*

fun getCurrentDate(): String {
    val calendar: Date = Calendar.getInstance().time
    val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    return dateFormat.format(calendar)
}

fun getDateBeforeToDay(): String {
    val date: Date = Calendar.getInstance().time
    val calendar = Calendar.getInstance()
    calendar.time = date
    calendar.add(Calendar.DATE, -2)
    val yesterday = calendar.time
    val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    return dateFormat.format(yesterday)
}