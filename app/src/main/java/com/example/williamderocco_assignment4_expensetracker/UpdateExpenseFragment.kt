package com.example.williamderocco_assignment4_expensetracker

import com.example.williamderocco_assignment4_expensetracker.R
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
import com.example.williamderocco_assignment4_expensetracker.databinding.FragmentUpdateExpenseBinding
import java.util.Calendar
import java.util.Date
import java.util.Locale.Category

private const val TAG = "UpdateExpenseFragment"

class UpdateExpenseFragment : Fragment() {
    private var _binding: FragmentUpdateExpenseBinding? = null
    private val binding
        get() = checkNotNull(_binding) {
            "Cannot access binding because it is null. Is the view visible?"
        }

    private val expenseListViewModel: ExpenseListViewModel by viewModels()
    private var id: Long? = 0L
    private var title: String? = ""
    private var amount: Double? = 0.0
    private var category: String? = ""
    private var selectedDate: Long? = 0L


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding =
            FragmentUpdateExpenseBinding.inflate(inflater, container, false)
        return binding.root
    }



    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Retrieve individual parameters from arguments
        id = arguments?.getLong("id")
        title = arguments?.getString("title")
        amount = arguments?.getDouble("amount")
        category = arguments?.getString("category")
        selectedDate = arguments?.getLong("selectedDate")

        // update the UI
        binding.nameEditText.hint = title
        binding.amountEditText.hint = amount.toString()

        // Initialize category spinner
        val categoryList = resources.getStringArray(R.array.category_list).toList()
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, categoryList)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.categorySpinner.adapter = adapter

        // Set spinner selection to category
        val categoryIndex = categoryList.indexOf(category)
        if (categoryIndex != -1) {
            binding.categorySpinner.setSelection(categoryIndex)
        }

        // Set date to selectedDate
        if (selectedDate != null) {
            val calendar = Calendar.getInstance().apply {
                timeInMillis = selectedDate!!
            }
            binding.datePicker.updateDate(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH))
        }

        // Set up listeners for amount EditText, category Spinner, date-picker, and update expense button
        setUpNameListener()
        setUpAmountListener()
        setUpCategoryListener()
        setUpDateListener()
        setUpUpdateExpenseListener()
        setCancelListener()
    }

    private fun setUpUpdateExpenseListener() {
        // Set up listener for add expense button
        binding.addExpenseButton.setOnClickListener {
            if (id == null) {
                Toast.makeText(requireContext(), "Some wacky stuff went wrong :/", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            // Check if any of the inputs are null
            if (title.isNullOrEmpty() || amount == null || category.isNullOrEmpty() || selectedDate == null) {
                Toast.makeText(requireContext(), "Please fill in all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            // Validate inputs
            if (title == "") {
                Toast.makeText(requireContext(), "Please enter a valid name", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (amount!! <= 0.0) {
                Toast.makeText(requireContext(), "Please enter a valid amount", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (category!!.isEmpty() || category == "All") {
                Toast.makeText(requireContext(), "Please select a valid category", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (selectedDate == 0L) {
                Toast.makeText(requireContext(), "Please select a date", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // update database and redirect user
            Toast.makeText(requireContext(), "Succesfully updated expense: " + title, Toast.LENGTH_SHORT).show()
            expenseListViewModel.updateExpense(id!!, title!!, amount!!, category!!, Date(selectedDate!!))
            findNavController().navigate(R.id.action_updateExpenseFragment_to_expenseListFragment)
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
            Log.d(TAG, "new date: " + selectedDate)
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

    private fun setCancelListener() {
        // Set up listener for cancel expense button
        binding.cancelButton.setOnClickListener {
            findNavController().navigate(R.id.action_updateExpenseFragment_to_expenseListFragment)
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}