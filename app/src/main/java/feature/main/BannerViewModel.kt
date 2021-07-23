package feature.main

import androidx.lifecycle.MutableLiveData
import com.example.myhome.R
import common.MyHomeSingleObserver
import common.MyHomeViewModel
import data.Banner
import data.SELL_OR_RENT
import data.State
import data.repository.BannerRepository
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import okhttp3.MultipartBody
import okhttp3.RequestBody

class BannerViewModel(val bannerRepository: BannerRepository, var CATE: Int) : MyHomeViewModel() {
    val bannerLiveData = MutableLiveData<List<Banner>>()

    //برای موقعی که دستبندی عوض میشه
    val categoryLiveData = MutableLiveData<Int>()
    val categories = arrayOf(R.string.cate1, R.string.cate2, R.string.cate3, R.string.cate4)

    init {
        getBanner()
        categoryLiveData.value = categories[CATE]
    }

    fun getBanner() {
        bannerRepository.getBanners(SELL_OR_RENT, CATE, "all")
            .asyncNetworkRequest()
            .subscribe(object : MyHomeSingleObserver<List<Banner>>(compositeDisposable) {
                override fun onSuccess(t: List<Banner>) {
                    bannerLiveData.value = t
                }
            })
    }

    fun chaneCategory(cate: Int) {
        this.CATE = cate
        this.categoryLiveData.value = categories[cate]
        getBanner()
    }


    fun deleteBanner(id: Int) = bannerRepository.deleteBanner(id)

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

    fun <T> Single<T>.asyncNetworkRequest(): Single<T> {
        //برای جلویری از تکرار این دو خط کد در هر بار گرفتن اطلاعات
        return subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }
}