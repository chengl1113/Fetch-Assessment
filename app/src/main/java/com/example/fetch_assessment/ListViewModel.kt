package com.example.fetch_assessment

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fetch_assessment.api.ApiClient
import com.example.fetch_assessment.models.Item
import kotlinx.coroutines.launch

private const val TAG = "ListViewModel"

class ListViewModel: ViewModel(){
    private val apiService = ApiClient.apiService
    val items: MutableState<List<Item>> = mutableStateOf(emptyList())
    val isLoading: MutableState<Boolean> = mutableStateOf(true)

    init {
        getItems()
    }
    private fun getItems() {
        viewModelScope.launch {
            isLoading.value = true
            try {
                val response = apiService.getItems()

                val filteredItems = response.filter { !it.name.isNullOrBlank()}

                val sortedItems = filteredItems
                    .groupBy { it.listId }
                    .toSortedMap() // sort by listId
                    .flatMap { (_,items) ->
                        items.sortedBy { it.name.toString().substring(5, it.name.toString().length).toInt() } // sorted by name
                    }
                items.value = sortedItems

                Log.d(TAG, "getItems: ${items.value}")
                isLoading.value = false

            } catch (e: Exception) {
                Log.e(TAG, e.message?: "found error")
                isLoading.value = false
            }
        }
    }
}