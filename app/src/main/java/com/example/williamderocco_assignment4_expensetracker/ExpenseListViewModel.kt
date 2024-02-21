package com.example.williamderocco_assignment4_expensetracker

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.room.Room
import com.example.williamderocco_assignment4_expensetracker.database.Expense
import com.example.williamderocco_assignment4_expensetracker.database.ExpenseDao
import com.example.williamderocco_assignment4_expensetracker.database.ExpenseDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Date

class ExpenseListViewModel(application: Application) : AndroidViewModel(application) {

    private val db = Room.databaseBuilder(
        getApplication(),
        ExpenseDatabase::class.java,
        "expense_database"
    ).build()

    private val expenseDao: ExpenseDao = db.expenseDao()

    // LiveData to hold the filtered expenses based on category
    private val _expenseList = MutableLiveData<List<Expense>>()
    val expenseList: LiveData<List<Expense>> = _expenseList

    // Function to fetch expenses by category
    fun fetchExpensesByCategory(category: String) {
        viewModelScope.launch(Dispatchers.IO) {
            _expenseList.postValue(expenseDao.getExpensesByCategory(category))
        }
    }
    fun fetchAllExpenses() {
        viewModelScope.launch(Dispatchers.IO) {
            _expenseList.postValue(expenseDao.getAllExpenses())
        }
    }

    fun addExpense(title: String, amount: Double, type: String, date: Date) {
        val expense = Expense(title = title, amount = amount, type = type, date = date)
        viewModelScope.launch(Dispatchers.IO) {
            expenseDao.insertExpense(expense)
        }
    }

    fun updateExpense(id: Long, newTitle: String, newAmount: Double, newType: String, newDate: Date) {
        viewModelScope.launch(Dispatchers.IO) {
            // Retrieve the existing expense by its ID
            val existingExpense = expenseDao.getExpenseById(id)

            // Create a new Expense object with updated values
            val updatedExpense = existingExpense.copy(
                // id not needed (copied from original object)
                title = newTitle,
                amount = newAmount,
                type = newType,
                date = newDate
            )

            // Update the existing expense in the database
            expenseDao.updateExpense(updatedExpense)
        }
    }

    fun deleteAllExpenses() {
        viewModelScope.launch(Dispatchers.IO) {
            expenseDao.deleteAllExpenses()
            // trigger UI update
            fetchAllExpenses()
        }
    }
}
