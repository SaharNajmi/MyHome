package data.repository.source

import data.Banner
import data.State
import io.reactivex.Completable
import io.reactivex.Single
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Part

interface BannerDataSource {

    fun getBanners(sellOrRent: Int, category: Int, phone: String): Single<List<Banner>>

    fun deleteBanner(id: Int): Single<State>

    fun getFavoriteBanners(): Single<List<Banner>>

    fun addToFavorites(): Completable

    fun deleteFromFavorites(): Completable

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
    ): Single<State>

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
    ): Single<State>
}