package com.example.williamderocco_assignment4_expensetracker.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "expenses")
data class Expense(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    var title: String,
    val amount: Double,
    val type: String,
    val date: Date
)
