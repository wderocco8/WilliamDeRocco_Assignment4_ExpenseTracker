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
    private var id: Long? = null
    private var title: String? = null
    private var amount: Double? = null
    private var category: String? = null
    private var selectedDate: Long? = null


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

        setCancelListener()
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