package com.example.myhome.data.repository.source

import com.example.myhome.data.Banner
import com.example.myhome.data.State
import com.example.myhome.services.ApiService
import io.reactivex.Completable
import io.reactivex.Single
import okhttp3.MultipartBody
import okhttp3.RequestBody

class BannerRemoteDataSource(private val apiService: ApiService) : BannerDataSource {
    override fun getBanners(
        sellOrRent: Int,
        category: Int,
        phone: String,
        price: String,
        homeSize: Int,
        numberOfRooms: Int
    ): Single<List<Banner>> =
        apiService.getAllBanner(sellOrRent, category, phone, price, homeSize, numberOfRooms)


    override fun deleteBanner(id: Int): Single<State> = apiService.deleteBanner(id)

    override fun getFavoriteBanners(): Single<List<Banner>> {
        TODO("Not yet implemented")
    }

    override fun addToFavorites(banner: Banner): Completable {
        TODO("Not yet implemented")
    }

    override fun deleteFromFavorites(banner: Banner): Completable {
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