package com.example.myhome.feature.main

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.myhome.common.MyHomeCompletableObserver
import com.example.myhome.common.MyHomeSingleObserver
import com.example.myhome.common.MyHomeViewModel
import com.example.myhome.common.Result
import com.example.myhome.common.asyncNetworkRequest
import com.example.myhome.data.model.Banner
import com.example.myhome.data.model.State
import com.example.myhome.data.repository.BannerRepository
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import okhttp3.MultipartBody
import okhttp3.RequestBody
import timber.log.Timber

class BannerViewModel(
    private val bannerRepository: BannerRepository,
    var category: Int,
    var sellOrRent: Int,
    var price: String,
    var homeSize: Int,
    var numberOfRooms: Int

) : MyHomeViewModel() {

    private val _deleteBannerResult = MutableLiveData<Result<State>>()
    val deleteBannerResult: LiveData<Result<State>> = _deleteBannerResult

    private val _selectedImageUri = MutableLiveData<Uri>()
    val selectedImageUri: LiveData<Uri> = _selectedImageUri

    private val _banners = MutableLiveData<List<Banner>>()
    val banners: LiveData<List<Banner>> = _banners

    init {
        getBanner()
    }

    fun getBanner() {
        bannerRepository.getBanners(sellOrRent, category, "all", price, homeSize, numberOfRooms)
            .asyncNetworkRequest()
            .subscribe(object : MyHomeSingleObserver<List<Banner>>(compositeDisposable) {
                override fun onSuccess(t: List<Banner>) {
                    _banners.postValue(t)
                }
            })
    }

    fun chaneCategory(cate: Int) {
        this.category = cate
        getBanner()
    }

    fun filter(price: String, numberOfRooms: Int, homeSize: Int) {
        this.price = price
        this.numberOfRooms = numberOfRooms
        this.homeSize = homeSize
        getBanner()
    }

    fun deleteBanner(id: Int) = bannerRepository.deleteBanner(id)
        .asyncNetworkRequest()
        .subscribe(object :
            MyHomeSingleObserver<State>(compositeDisposable) {
            override fun onSuccess(t: State) {
                _deleteBannerResult.value = Result.Success(t)
            }

            override fun onSubscribe(d: Disposable) {
                super.onSubscribe(d)
                _deleteBannerResult.value = Result.Loading
            }

            override fun onError(e: Throwable) {
                super.onError(e)
                _deleteBannerResult.value = Result.Error(e)
            }
        })

    fun editBanner(
        id: Int,
        userID: Int,
        title: RequestBody,
        description: RequestBody,
        price: RequestBody,
        location: RequestBody,
        category: Int,
        sellOrRent: Int,
        homeSize: Int,
        numberOfRooms: Int,
        image: MultipartBody.Part?
    ): Single<State> = bannerRepository.editBanner(
        id,
        userID,
        title,
        description,
        price,
        location,
        category,
        sellOrRent,
        homeSize,
        numberOfRooms,
        image
    )

    fun addBanner(
        userID: Int,
        title: RequestBody,
        description: RequestBody,
        price: RequestBody,
        location: RequestBody,
        category: Int,
        sellOrRent: Int,
        homeSize: Int,
        numberOfRooms: Int,
        image: MultipartBody.Part?
    ): Single<State> = bannerRepository.addBanner(
        userID,
        title,
        description,
        price,
        location,
        category,
        sellOrRent,
        homeSize,
        numberOfRooms,
        image
    )

    fun addBannerToFavorite(banner: Banner) {
        if (banner.fav)
            bannerRepository.addToFavorites(banner)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : MyHomeCompletableObserver(compositeDisposable) {
                    override fun onComplete() {
                        banner.fav = true
                        Timber.i("add fav")
                    }
                })
        else
            bannerRepository.deleteFromFavorites(banner)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : MyHomeCompletableObserver(compositeDisposable) {
                    override fun onComplete() {
                        banner.fav = false
                        Timber.i("delete fav")
                    }
                })
    }

    fun setImageUri(uri: Uri?) {
        _selectedImageUri.value = uri
    }
}