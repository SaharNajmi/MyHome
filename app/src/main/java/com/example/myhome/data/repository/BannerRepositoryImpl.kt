package com.example.myhome.data.repository

import com.example.myhome.data.model.Banner
import com.example.myhome.data.model.State
import com.example.myhome.data.repository.source.BannerDataSource
import io.reactivex.Completable
import io.reactivex.Single
import okhttp3.MultipartBody
import okhttp3.RequestBody
import timber.log.Timber

class BannerRepositoryImpl(
    private val bannerRemoteDataSource: BannerDataSource,
    private val bannerLocalDataSource: BannerDataSource
) : BannerRepository {

    override fun getBanners(
        sellOrRent: Int,
        category: Int,
        phone: String,
        price: String,
        homeSize: Int,
        numberOfRooms: Int
    ): Single<List<Banner>> = bannerLocalDataSource.getFavoriteBanners().flatMap { favoriteBanner ->
        bannerRemoteDataSource.getBanners(
            sellOrRent,
            category,
            phone,
            price,
            homeSize,
            numberOfRooms
        ).doOnSuccess {
            //get all favorite Ids
            val favIds = favoriteBanner.map { banner ->
                banner.id
            }
            //if favId == bannerId from server -> there is item in favorite
            it.forEach { banner ->
                if (favIds.contains(banner.id))
                    banner.fav = true
            }
        }
    }

    override fun deleteBanner(id: Int): Single<State> = bannerRemoteDataSource.deleteBanner(id)

    override fun getFavoriteBanners(): Single<List<Banner>> =
        bannerLocalDataSource.getFavoriteBanners()

    override fun addToFavorites(banner: Banner): Completable =
        bannerLocalDataSource.addToFavorites(banner)


    override fun deleteFromFavorites(banner: Banner): Completable =
        bannerLocalDataSource.deleteFromFavorites(banner)

    override fun editBanner(
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
    ): Single<State> = bannerRemoteDataSource.editBanner(
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
    ).doOnSuccess {
        Timber.i("edit banner: ${it.state}")
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
    ): Single<State> = bannerRemoteDataSource.addBanner(
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
    ).doOnSuccess {
        Timber.i("add banner: ${it.state}")
    }
}