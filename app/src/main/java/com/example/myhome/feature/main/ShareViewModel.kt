package com.example.myhome.feature.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ShareViewModel : ViewModel() {
    private val _category: MutableLiveData<Int> = MutableLiveData()
    val category: LiveData<Int> = _category

    private val _search: MutableLiveData<String> = MutableLiveData()
    val search: LiveData<String> = _search

    private val _filter: MutableLiveData<ArrayList<Any>> = MutableLiveData()
    val filter: LiveData<ArrayList<Any>> = _filter

    fun changeCategory(categorySelected: Int) {
        _category.value = categorySelected
    }

    fun changeTextSearch(input: String) {
        _search.value = input
    }

    fun setDataFilter(price: String, numberOfRooms: Int, homeSize: Int) {
        val array: ArrayList<Any> = arrayListOf(price, numberOfRooms, homeSize)
        _filter.value = array
    }
}