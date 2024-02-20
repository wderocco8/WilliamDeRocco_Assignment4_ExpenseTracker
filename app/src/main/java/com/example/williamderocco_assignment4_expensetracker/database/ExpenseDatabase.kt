package com.example.williamderocco_assignment4_expensetracker.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = [Expense::class], version = 1)
@TypeConverters(Converters::class)
abstract class ExpenseDatabase : RoomDatabase() {
    abstract fun expenseDao(): ExpenseDao

    companion object {
        // Define a private instance variable
        @Volatile
        private var instance: ExpenseDatabase? = null

    }

}
