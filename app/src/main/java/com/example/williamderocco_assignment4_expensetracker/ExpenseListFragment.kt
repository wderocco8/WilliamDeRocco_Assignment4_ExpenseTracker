package com.example.williamderocco_assignment4_expensetracker

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.williamderocco_assignment4_expensetracker.database.Expense
import com.example.williamderocco_assignment4_expensetracker.databinding.FragmentExpenseListBinding

class ExpenseListFragment : Fragment(), ExpenseListAdapter.OnItemClickListener {

    private var _binding: FragmentExpenseListBinding? = null
    private val binding
        get() = checkNotNull(_binding) {
            "Cannot access binding because it is null. Is the view visible?"
        }

    private val expenseListViewModel: ExpenseListViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentExpenseListBinding.inflate(inflater, container, false)

        // Initialize RecyclerView
        binding.expenseRecyclerView.layoutManager = LinearLayoutManager(context)
        val adapter = ExpenseListAdapter(emptyList(), this) // pass in `this` as listener for ExpenseListAdapter (allows us to synchronize data when the expenseList updates)
        binding.expenseRecyclerView.adapter = adapter

        // Initialize Spinner
        val categorySpinner = binding.categorySpinner
        val categoryList = listOf("All", "Food", "Entertainment", "Housing", "Utilities", "Fuel", "Automotive", "Misc") // expense categories
        val spinnerAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, categoryList)
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        categorySpinner.adapter = spinnerAdapter

        // Spinner item selection listener
        categorySpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val selectedCategory = categoryList[position]
                if (selectedCategory == "All") {
                    expenseListViewModel.fetchAllExpenses()
                } else {
                    expenseListViewModel.fetchExpensesByCategory(selectedCategory)
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Do nothing
            }
        }

        // Observe changes to the newsList LiveData
        expenseListViewModel.expenseList.observe(viewLifecycleOwner) { expenseList ->
            adapter.updateExpenseList(expenseList)
        }

        return binding.root
    }

    override fun onItemClick(expense: Expense) {
        // TO-DO: figure out what to do when clicking on an expense

//        val bundle = Bundle().apply {
//            // Pass individual parameters
//            putString("title", news.title)
//            putString("description", news.description)
//            putString("content", news.content)
//            putString("imageUrl", news.imageUrl)
//        }
//        // Navigate to NewsDetailFragment using NavController
//        findNavController().navigate(
//            R.id.action_newsListFragment_to_fragmentNewsDetail,
//            bundle
//        )
    }



    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}