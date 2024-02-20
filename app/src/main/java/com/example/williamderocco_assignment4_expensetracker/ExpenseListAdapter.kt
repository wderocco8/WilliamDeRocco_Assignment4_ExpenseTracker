package com.example.williamderocco_assignment4_expensetracker

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.williamderocco_assignment4_expensetracker.database.Expense
import com.example.williamderocco_assignment4_expensetracker.databinding.ListItemExpenseBinding

private const val TAG = "NewsListAdapter"

class ExpenseHolder(
    private val binding: ListItemExpenseBinding
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(expense: Expense) {
        binding.expenseTitle.text = expense.title
        binding.expenseAmount.text = expense.amount.toString()
        binding.expenseType.text = expense.type
        binding.expenseDate.text = expense.date.toString()

        binding.root.setOnClickListener {
            Toast.makeText(
                binding.root.context,
                "${expense.title} clicked!",
                Toast.LENGTH_SHORT
            ).show()
        }
    }
}

class ExpenseListAdapter(private var expenseList: List<Expense>, private val listener: OnItemClickListener) : RecyclerView.Adapter<ExpenseHolder>() {
    interface OnItemClickListener {
        fun onItemClick(expense: Expense)
    }

    fun updateExpenseList(newList: List<Expense>) {
        expenseList = newList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExpenseHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ListItemExpenseBinding.inflate(inflater, parent, false)
        return ExpenseHolder(binding)
    }

    override fun onBindViewHolder(holder: ExpenseHolder, position: Int) {
        val expense = expenseList[position]
        holder.bind(expense)

        holder.itemView.setOnClickListener {
            listener.onItemClick(expense)
        }
    }

    override fun getItemCount() = expenseList.size
}

