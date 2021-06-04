package main

import androidx.lifecycle.MutableLiveData
import data.Banner
import data.repository.BannerRepository
import io.reactivex.SingleObserver
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import timber.log.Timber


class MainViewModel(bannerRepository: BannerRepository) : MyHomeViewModel() {
    val bannerLiveData = MutableLiveData<List<Banner>>()

    init {
        //وقتی کانستراکتورش call شد ریکوست به سرور میفرستیم و لیست داده ها را از سرور مییگیریم
        bannerRepository.getBanners()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : SingleObserver<List<Banner>> {
                override fun onSuccess(t: List<Banner>) {
                    bannerLiveData.value = t
                }

                override fun onSubscribe(d: Disposable) {
                    compositeDisposable.add(d)
                }

                override fun onError(e: Throwable) {
                    Timber.e(e)
                }
            })
    }
}