package com.example.myhome.feature.main

import android.os.Bundle
import androidx.lifecycle.MutableLiveData
import com.example.myhome.common.EXTRA_KEY_DATA
import com.example.myhome.common.MyHomeViewModel
import com.example.myhome.data.Banner

class BannerDetailViewModel(val bundle: Bundle) : MyHomeViewModel() {

    val bannerLiveData = MutableLiveData<Banner>()

    init {
        bannerLiveData.value = bundle.getParcelable(EXTRA_KEY_DATA)
    }
}