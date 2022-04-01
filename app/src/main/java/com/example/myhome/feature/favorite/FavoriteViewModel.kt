package com.example.myhome.feature.favorite

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.myhome.common.MyHomeCompletableObserver
import com.example.myhome.common.MyHomeSingleObserver
import com.example.myhome.common.MyHomeViewModel
import com.example.myhome.common.asyncNetworkRequest
import com.example.myhome.data.model.Banner
import com.example.myhome.data.repository.BannerRepository
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import timber.log.Timber

class FavoriteViewModel(private val bannerRepository: BannerRepository) : MyHomeViewModel() {
    private val _banners = MutableLiveData<List<Banner>>()
    val banners: LiveData<List<Banner>> = _banners

    init {
        getFavorite()
    }

    fun getFavorite() {
        bannerRepository.getFavoriteBanners()
            .asyncNetworkRequest()
            .subscribe(object : MyHomeSingleObserver<List<Banner>>(compositeDisposable) {
                override fun onSuccess(t: List<Banner>) {
                    _banners.postValue(t)
                }
            })
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