package feature.main

import androidx.lifecycle.MutableLiveData
import com.example.myhome.R
import common.MyHomeCompletableObserver
import common.MyHomeSingleObserver
import common.MyHomeViewModel
import data.Banner
import data.State
import data.repository.BannerRepository
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import okhttp3.MultipartBody
import okhttp3.RequestBody
import timber.log.Timber

class BannerViewModel(
    val bannerRepository: BannerRepository,
    var category: Int,
    var sellOrRent: Int,
    var price: String,
    var homeSize: Int,
    var numberOfRooms: Int
) :
    MyHomeViewModel() {
    val bannerLiveData = MutableLiveData<List<Banner>>()
    //برای موقعی که دستبندی عوض میشه
    val categoryLiveData = MutableLiveData<Int>()
    val categories = arrayOf(R.string.cate1, R.string.cate2, R.string.cate3, R.string.cate4)

    init {
        getBanner()
        categoryLiveData.value = categories[category]
    }

    fun getBanner() {
        bannerRepository.getBanners(sellOrRent, category, "all", price, homeSize, numberOfRooms)
            .asyncNetworkRequest()
            .subscribe(object : MyHomeSingleObserver<List<Banner>>(compositeDisposable) {
                override fun onSuccess(t: List<Banner>) {
                    bannerLiveData.value = t
                }
            })
    }

    fun refresh() {
        getBanner()
    }

    fun chaneCategory(cate: Int) {
        this.category = cate
        this.categoryLiveData.value = categories[cate - 1]
        getBanner()
    }

    fun filter(price: String, numberOfRooms: Int, homeSize: Int) {
        this.price = price
        this.numberOfRooms = numberOfRooms
        this.homeSize = homeSize
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

    fun <T> Single<T>.asyncNetworkRequest(): Single<T> {
        //برای جلویری از تکرار این دو خط کد در هر بار گرفتن اطلاعات
        return subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }
}