package feature.main

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ShareViewModel :ViewModel(){

    // Mutable LiveData which observed by LiveData
    private val mutableLiveData: MutableLiveData<Int> = MutableLiveData()

    // function to set the changed
    fun setData(input: Int) {
        mutableLiveData.value = input
    }

    // function to get the changed data from the EditTexts
    fun getData(): MutableLiveData<Int> = mutableLiveData
}