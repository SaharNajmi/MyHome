package feature.main

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ShareViewModel : ViewModel() {

    // Mutable LiveData which observed by LiveData
    private val mutableLiveDataCategory: MutableLiveData<Int> = MutableLiveData()
    private val mutableLiveDataSearch: MutableLiveData<String> = MutableLiveData()
    private val mutableLiveDataFilter: MutableLiveData<ArrayList<Any>> = MutableLiveData()

    /*----------------------Category---------------------------*/
    // function to set the changed
    fun setDataCategory(input: Int) {
        mutableLiveDataCategory.value = input
    }

    // function to get the changed data from the EditTexts
    fun getDataCategory(): MutableLiveData<Int> = mutableLiveDataCategory

    /*----------------------Search---------------------------*/
    fun setDataSearch(input: String) {
        mutableLiveDataSearch.value = input
    }

    fun getDataSearch(): MutableLiveData<String> = mutableLiveDataSearch

    /*----------------------filter---------------------------*/
    fun setDataFilter(price: String, numberOfRooms: Int, homeSize: Int) {
        var array: ArrayList<Any> = arrayListOf(price, numberOfRooms, homeSize)
        mutableLiveDataFilter.value = array
    }

    fun getDataFilter() = mutableLiveDataFilter
}