package main

import android.os.Bundle
import androidx.lifecycle.MutableLiveData
import common.EXTRA_KEY_DATA
import common.MyHomeViewModel
import data.Banner

class BannerDetailViewModel(val bundle: Bundle) : MyHomeViewModel() {

    val bannerLiveData = MutableLiveData<Banner>()

    init {
        bannerLiveData.value = bundle.getParcelable(EXTRA_KEY_DATA)
    }
}