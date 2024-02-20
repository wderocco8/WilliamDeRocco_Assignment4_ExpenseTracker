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


    init {
        // Initialize the database with sample records when the ViewModel is created
//        initializeDatabase()
    }

    private fun initializeDatabase() {
        viewModelScope.launch(Dispatchers.IO) {
            // Check if the database is empty
            if (expenseDao.getExpenseCount() == 0) {
                // Insert 5 sample expenses
                insertSampleExpenses()
            }
        }
    }

    private fun insertSampleExpenses() {
        val sampleExpenses = listOf(
            Expense(1, "expense 1",20.0,  "Food", Date()),
            Expense(2, "expense 2", 10.0, "Transportation", Date()),
            Expense(4, "expense 3",1.0, "Entertainment", Date()),
            Expense(5, "expense 4",15.0, "Shopping", Date()),
            Expense(6, "expense 5",16.0, "Utilities", Date())
        )
        for (expense in sampleExpenses) {
            viewModelScope.launch(Dispatchers.IO){
                expenseDao.insertExpense(expense)
            }
        }
    }




}
