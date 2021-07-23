package data.repository.source

import data.Banner
import data.State
import io.reactivex.Completable
import io.reactivex.Single
import okhttp3.MultipartBody
import okhttp3.RequestBody
import services.ApiService

class BannerRemoteDataSource(val apiService: ApiService) : BannerDataSource {
    override fun getBanners(
        sellOrRent: Int,
        category: Int,
        phone: String
    ): Single<List<Banner>> {
        return apiService.getAllBanner(sellOrRent, category, phone)
    }

    override fun deleteBanner(id: Int): Single<State> = apiService.deleteBanner(id)

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
    ): Single<State> = apiService.editBanner(
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
    ): Single<State> = apiService.addBanner(
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

}