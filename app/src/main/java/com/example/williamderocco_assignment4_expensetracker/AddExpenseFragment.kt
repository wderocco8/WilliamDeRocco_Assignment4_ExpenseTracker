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
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
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

    private val expenseListViewModel: ExpenseListViewModel by viewModels()

    private var title: String = "" // Variable to hold the amount
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

        // Set up listeners for amount EditText, category Spinner, date-picker, and add expense button
        setUpAmountListener()
        setUpCategoryListener()
        setUpDateListener()
        setUpAddExpenseListener()
        setUpNameListener()

        // Set current date to date picker
        val currentDate = Calendar.getInstance()
        val year = currentDate.get(Calendar.YEAR)
        val month = currentDate.get(Calendar.MONTH)
        val day = currentDate.get(Calendar.DAY_OF_MONTH)
        binding.datePicker.init(year, month, day, null)
        selectedDate = currentDate.timeInMillis
    }

    private fun setUpAddExpenseListener() {
        // Set up listener for add expense button
        binding.addExpenseButton.setOnClickListener {
            // Validate inputs
            if (title == "") {
                Toast.makeText(requireContext(), "Please enter a valid name", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
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

            Toast.makeText(requireContext(), "Succesfully added expense: " + title, Toast.LENGTH_SHORT).show()
            // update database
            expenseListViewModel.addExpense(title, amount, category, Date(selectedDate))

            findNavController().navigate(com.example.williamderocco_assignment4_expensetracker.R.id.action_addExpenseFragment_to_expenseListFragment)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun setUpDateListener() {
        // Set up listener for date picker
        binding.datePicker.setOnDateChangedListener { _, year, monthOfYear, dayOfMonth ->
            val calendar = Calendar.getInstance().apply {
                set(Calendar.YEAR, year)
                set(Calendar.MONTH, monthOfYear)
                set(Calendar.DAY_OF_MONTH, dayOfMonth)
            }
            selectedDate = calendar.timeInMillis
        }
    }

    private fun setUpAmountListener() {
        binding.amountEditText.doOnTextChanged { text, _, _, _ ->
            amount = text.toString().toDoubleOrNull() ?: 0.0
        }
    }

    private fun setUpNameListener() {
        binding.nameEditText.doOnTextChanged { text, _, _, _ ->
            title = text.toString()
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