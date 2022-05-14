package com.example.myhome.feature.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ShareViewModel : ViewModel() {
    private val _category: MutableLiveData<Int> = MutableLiveData()
    val category: LiveData<Int> = _category

    private val _filterResult: MutableLiveData<ArrayList<Any>> = MutableLiveData()
    val filterResult: LiveData<ArrayList<Any>> = _filterResult

    private val _filterPrice: MutableLiveData<String> = MutableLiveData()
    val filterPrice: LiveData<String> = _filterPrice

    private val _filterNumberOfRooms: MutableLiveData<Int> = MutableLiveData()
    val filterNumberOfRooms: LiveData<Int> = _filterNumberOfRooms

    private val _filterHomeSize: MutableLiveData<Int> = MutableLiveData()
    val filterHomeSize: LiveData<Int> = _filterHomeSize

    private val _search: MutableLiveData<String> = MutableLiveData()
    val search: LiveData<String> = _search

    fun changeCategory(categorySelected: Int) {
        _category.value = categorySelected
    }

    fun changeTextSearch(input: String) {
        _search.value = input
    }

    fun setDataFilter(price: String, numberOfRooms: Int, homeSize: Int) {
        val array: ArrayList<Any> = arrayListOf(price, numberOfRooms, homeSize)
        _filterResult.value = array
        _filterPrice.value = price
        _filterNumberOfRooms.value = numberOfRooms
        _filterHomeSize.value = homeSize
    }
}