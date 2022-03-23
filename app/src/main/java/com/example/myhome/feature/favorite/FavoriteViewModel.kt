package com.example.myhome.feature.favorite

import androidx.lifecycle.MutableLiveData
import com.example.myhome.common.MyHomeCompletableObserver
import com.example.myhome.common.MyHomeSingleObserver
import com.example.myhome.common.MyHomeViewModel
import com.example.myhome.data.Banner
import com.example.myhome.data.repository.BannerRepository
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import timber.log.Timber

class FavoriteViewModel(val bannerRepository: BannerRepository) : MyHomeViewModel() {
    val bannerLiveData = MutableLiveData<List<Banner>>()

    init {
        getFavorite()
    }

    fun getFavorite() {
        bannerRepository.getFavoriteBanners()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : MyHomeSingleObserver<List<Banner>>(compositeDisposable) {
                override fun onSuccess(t: List<Banner>) {
                    bannerLiveData.postValue(t)
                }
            })
    }

    fun refresh() {
        getFavorite()
    }

    fun addFavorites(banner: Banner) {
        bannerRepository.addToFavorites(banner)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : MyHomeCompletableObserver(compositeDisposable) {
                override fun onComplete() {
                    Timber.i("add fav")
                }
            })
    }


    fun deleteFavorites(banner: Banner) {
        bannerRepository.deleteFromFavorites(banner)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : MyHomeCompletableObserver(compositeDisposable) {
                override fun onComplete() {
                    Timber.i("del fav")
                }
            })
    }
}