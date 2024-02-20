package com.example.williamderocco_assignment4_expensetracker

import java.util.Date

data class Expense(
    val title: String,
    val amount: Number,
    val type: String,
    val date: Date
)