package main

import androidx.lifecycle.MutableLiveData
import common.MyHomeSingleObserver
import common.MyHomeViewModel
import data.Banner
import data.repository.BannerRepository
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers


class MainViewModel(bannerRepository: BannerRepository) : MyHomeViewModel() {
    val bannerLiveData = MutableLiveData<List<Banner>>()

    init {
        //وقتی کانستراکتورش call شد ریکوست به سرور میفرستیم و لیست داده ها را از سرور مییگیریم
        bannerRepository.getBanners()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : MyHomeSingleObserver<List<Banner>>(compositeDisposable) {
                override fun onSuccess(t: List<Banner>) {
                    bannerLiveData.value = t
                }
            })
    }
}