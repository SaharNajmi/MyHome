package com.example.myhome.feature.main

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.myhome.common.*
import com.example.myhome.data.model.Banner
import com.example.myhome.data.model.State
import com.example.myhome.data.repository.BannerRepository
import io.reactivex.SingleObserver
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import okhttp3.MultipartBody
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

    private val _editBannerResult = MutableLiveData<Result<State>>()
    val editBannerResult: LiveData<Result<State>> = _editBannerResult

    private val _addBannerResult = MutableLiveData<Result<State>>()
    val addBannerResult: LiveData<Result<State>> = _addBannerResult

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
                _deleteBannerResult.value = Result.Loading
                super.onSubscribe(d)
            }

            override fun onError(e: Throwable) {
                _deleteBannerResult.value = Result.Error(e)
                super.onError(e)
            }
        })

    fun editBanner(
        id: Int,
        userID: Int,
        title: String,
        description: String,
        price: String,
        location: String,
        category: Int,
        sellOrRent: Int,
        homeSize: Int,
        numberOfRooms: Int,
        image: MultipartBody.Part?
    ) = bannerRepository.editBanner(
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
    ).asyncNetworkRequest()
        .subscribe(object :
            SingleObserver<State> {
            override fun onSuccess(t: State) {
                _editBannerResult.value = Result.Success(t)
            }

            override fun onSubscribe(d: Disposable) {
                compositeDisposable.add(d)
                _editBannerResult.value = Result.Loading
            }

            override fun onError(e: Throwable) {
                _editBannerResult.value = Result.Error(e)
            }
        })

    fun addBanner(
        userID: Int,
        title: String,
        description: String,
        price: String,
        location: String,
        category: Int,
        sellOrRent: Int,
        homeSize: Int,
        numberOfRooms: Int,
        image: MultipartBody.Part?
    ) = bannerRepository.addBanner(
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
    ).asyncNetworkRequest()
        .subscribe(object :
            SingleObserver<State> {
            override fun onSuccess(t: State) {
                if (t.state)
                    _addBannerResult.value = Result.Success(t)
            }

            override fun onSubscribe(d: Disposable) {
                compositeDisposable.add(d)
                _addBannerResult.value = Result.Loading
            }

            override fun onError(e: Throwable) {
                _addBannerResult.value = Result.Error(e)
            }
        })

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