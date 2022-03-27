package com.example.myhome.data.repository.source

import androidx.room.*
import com.example.myhome.data.Banner
import com.example.myhome.data.State
import io.reactivex.Completable
import io.reactivex.Single
import okhttp3.MultipartBody
import okhttp3.RequestBody

@Dao
interface BannerLocalDataSource : BannerDataSource {
    override fun getBanners(
        sellOrRent: Int,
        category: Int,
        phone: String,
        price: String,
        homeSize: Int,
        numberOfRooms: Int
    ): Single<List<Banner>> {
        TODO("Not yet implemented")
    }

    override fun deleteBanner(id: Int): Single<State> {
        TODO("Not yet implemented")
    }

    @Query("SELECT * FROM banners ")
    override fun getFavoriteBanners(): Single<List<Banner>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    override fun addToFavorites(banner: Banner): Completable

    @Delete()
    override fun deleteFromFavorites(banner: Banner): Completable

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