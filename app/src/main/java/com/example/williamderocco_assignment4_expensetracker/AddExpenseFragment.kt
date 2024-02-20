package com.example.williamderocco_assignment4_expensetracker

import android.R
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.widget.doOnTextChanged
import com.example.williamderocco_assignment4_expensetracker.database.Expense
import com.example.williamderocco_assignment4_expensetracker.databinding.FragmentAddExpenseBinding
import java.util.Calendar
import java.util.Date

private const val TAG = "AddExpenseFragment"

class AddExpenseFragment : Fragment() {
    private var _binding: FragmentAddExpenseBinding? = null
    private val binding
        get() = checkNotNull(_binding) {
            "Cannot access binding because it is null. Is the view visible?"
        }

    private var amount: Double = 0.0 // Variable to hold the amount
    private var category: String = "" // Variable to hold the category
    private var selectedDate: Long = 0 // Variable to hold the selected date in milliseconds


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding =
            FragmentAddExpenseBinding.inflate(inflater, container, false)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize category spinner
        val categoryList = listOf("Food", "Entertainment", "Housing", "Utilities", "Fuel", "Automotive", "Misc")
        val adapter = ArrayAdapter(requireContext(), R.layout.simple_spinner_item, categoryList)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.categorySpinner.adapter = adapter

        // Set up listeners for amount EditText and category Spinner
        setUpAmountListener()
        setUpCategoryListener()

        // Set up listener for date picker
        binding.datePicker.setOnDateChangedListener { _, year, monthOfYear, dayOfMonth ->
            val calendar = Calendar.getInstance().apply {
                set(Calendar.YEAR, year)
                set(Calendar.MONTH, monthOfYear)
                set(Calendar.DAY_OF_MONTH, dayOfMonth)
            }
            selectedDate = calendar.timeInMillis
        }

        // Set up listener for add expense button
        binding.addExpenseButton.setOnClickListener {
            // Validate inputs
            if (amount <= 0.0) {
                Toast.makeText(requireContext(), "Please enter a valid amount", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (category.isEmpty()) {
                Toast.makeText(requireContext(), "Please select a category", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (selectedDate == 0L) {
                Toast.makeText(requireContext(), "Please select a date", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Create Expense object with the input values
            val expense = Expense(
                title = "Expense", // You can set a default title or get it from another source
                amount = amount,
                type = category,
                date = Date(selectedDate)
            )

            // Do something with the expense object, such as adding it to the database
            // You can call a function in your ViewModel to handle this
            // viewModel.addExpense(expense)
        }
    }

    private fun setUpAmountListener() {
        binding.amountEditText.doOnTextChanged { text, _, _, _ ->
            amount = text.toString().toDoubleOrNull() ?: 0.0
        }
    }

    private fun setUpCategoryListener() {
        binding.categorySpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                category = parent?.getItemAtPosition(position).toString()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Do nothing
            }
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}