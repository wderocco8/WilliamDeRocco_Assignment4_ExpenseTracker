package com.example.williamderocco_assignment4_expensetracker

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ExpenseListViewModel : ViewModel() {

    private val _expenseList = MutableLiveData<List<Expense>>()
    val expenseList: LiveData<List<Expense>> = _expenseList

//    init {
//        viewModelScope.launch {
//
//            fetchTopNews(newsApiService)?.let { articles ->
//                // Update the newsList with the fetched articles
//                // For example, you can add them to the existing list
//                _newsList.value = articles
//                Log.d(TAG, "articles" + articles.toString())
//            }
//        }
//    }

//    // Function to fetch news by category
//    fun fetchNewsByCategory(category: String) {
//        viewModelScope.launch {
//            // Initialize newsApiService inside the coroutine
//            val newsApiService = RetrofitService.newsApiService
//
//            fetchTopNews(newsApiService, category)?.let { articles ->
//                // Update the newsList with the fetched articles
//                // For example, you can add them to the existing list
//                _newsList.value = articles
//            }
//
//        }
//    }
//
//    private suspend fun fetchTopNews(newsApiService: NewsApiService, category: String = ""): List<News>? {
//        return withContext(Dispatchers.IO) {
//            try {
//                val response = newsApiService.getTopNewsByCategory(category).execute()
//                if (response.isSuccessful) {
//                    response.body()?.articles
//                } else {
//                    // Handle unsuccessful response
//                    null
//                }
//            } catch (e: Exception) {
//                // Handle network errors
//                Log.d(TAG, "encountered error: ", e)
//                null
//            }
//        }
//    }
}