package com.example.myhome.feature.main

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ShareViewModel : ViewModel() {

    private val mutableLiveDataCategory: MutableLiveData<Int> = MutableLiveData()
    private val mutableLiveDataSearch: MutableLiveData<String> = MutableLiveData()
    private val mutableLiveDataFilter: MutableLiveData<ArrayList<Any>> = MutableLiveData()

    fun setDataCategory(input: Int) {
        mutableLiveDataCategory.value = input
    }

    fun getDataCategory(): MutableLiveData<Int> = mutableLiveDataCategory

    fun setDataSearch(input: String) {
        mutableLiveDataSearch.value = input
    }

    fun getDataSearch(): MutableLiveData<String> = mutableLiveDataSearch

    fun setDataFilter(price: String, numberOfRooms: Int, homeSize: Int) {
        val array: ArrayList<Any> = arrayListOf(price, numberOfRooms, homeSize)
        mutableLiveDataFilter.value = array
    }

    fun getDataFilter() = mutableLiveDataFilter
}