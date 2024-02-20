package com.example.williamderocco_assignment4_expensetracker.database

import androidx.room.TypeConverter
import java.util.Date

object Converters {
    @JvmStatic
    @TypeConverter
    fun fromDate(value: Long?): Date? {
        return value?.let { Date(it) }
    }

    @JvmStatic
    @TypeConverter
    fun toDate(date: Date?): Long? {
        return date?.time
    }
}
