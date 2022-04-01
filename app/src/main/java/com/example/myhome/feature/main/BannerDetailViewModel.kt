package com.example.myhome.feature.main

import android.os.Bundle
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.myhome.common.Constants.EXTRA_KEY_DATA
import com.example.myhome.common.MyHomeViewModel
import com.example.myhome.data.model.Banner

class BannerDetailViewModel(val bundle: Bundle) : MyHomeViewModel() {

    private val _banner = MutableLiveData<Banner>()
    val banner: LiveData<Banner> = _banner

    init {
        _banner.value = bundle.getParcelable(EXTRA_KEY_DATA)
    }
}