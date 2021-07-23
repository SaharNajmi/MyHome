package data.repository.source

import data.Banner
import data.State
import io.reactivex.Completable
import io.reactivex.Single
import okhttp3.MultipartBody
import okhttp3.RequestBody

class BannerLocalDataSource : BannerDataSource {
    override fun getBanners(sellOrRent: Int, category: Int, phone: String): Single<List<Banner>> {
        TODO("Not yet implemented")
    }

    override fun deleteBanner(id: Int): Single<State> {
        TODO("Not yet implemented")
    }

    override fun getFavoriteBanners(): Single<List<Banner>> {
        TODO("Not yet implemented")
    }

    override fun addToFavorites(): Completable {
        TODO("Not yet implemented")
    }

    override fun deleteFromFavorites(): Completable {
        TODO("Not yet implemented")
    }

    override fun editBanner(
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
    ): Single<State> {
        TODO("Not yet implemented")
    }

    override fun addBanner(
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
    ): Single<State> {
        TODO("Not yet implemented")
    }
}