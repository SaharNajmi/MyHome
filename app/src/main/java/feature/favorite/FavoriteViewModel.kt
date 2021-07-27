package feature.favorite

import androidx.lifecycle.MutableLiveData
import common.MyHomeCompletableObserver
import common.MyHomeSingleObserver
import common.MyHomeViewModel
import data.Banner
import data.repository.BannerRepository
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