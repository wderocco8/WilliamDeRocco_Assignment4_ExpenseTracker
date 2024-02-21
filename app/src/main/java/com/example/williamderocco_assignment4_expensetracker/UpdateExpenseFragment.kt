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

    private var newTitle: String = "" // Variable to hold the amount
    private var newAmount: Double = 0.0 // Variable to hold the amount
    private var newCategory: String = "" // Variable to hold the category
    private var newSelectedDate: Long = 0 // Variable to hold the selected date in milliseconds


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
        val id = arguments?.getLong("id")
        val title = arguments?.getString("title")
        val amount = arguments?.getString("amount")
        val category = arguments?.getString("category")
        val selectedDate = arguments?.getString("selectedDate")

        binding.nameEditText.hint = title
        binding.amountEditText.hint = amount

        // Initialize category spinner
        val categoryList = listOf("Food", "Entertainment", "Housing", "Utilities", "Fuel", "Automotive", "Misc")
        val adapter = ArrayAdapter(requireContext(), R.layout.simple_spinner_item, categoryList)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.categorySpinner.adapter = adapter

        Log.d(TAG, "category: " + category)
        // Set spinner selection to category
        val categoryIndex = categoryList.indexOf(category)
        Log.d(TAG, "category: " + categoryIndex)

        if (categoryIndex != -1) {
            binding.categorySpinner.setSelection(categoryIndex)
        }

        setCancelListener()
    }



    private fun setCancelListener() {
        // Set up listener for cancel expense button
        binding.cancelButton.setOnClickListener {
            findNavController().navigate(com.example.williamderocco_assignment4_expensetracker.R.id.action_updateExpenseFragment_to_expenseListFragment)
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}