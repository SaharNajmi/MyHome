package com.example.myhome.data.repository.source.local

import androidx.room.*
import com.example.myhome.data.model.Banner
import com.example.myhome.data.model.State
import com.example.myhome.data.repository.source.BannerDataSource
import io.reactivex.Completable
import io.reactivex.Single
import okhttp3.MultipartBody

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
        title: String,
        description: String,
        price: String,
        location: String,
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
        title: String,
        description: String,
        price: String,
        location: String,
        category: Int,
        sellOrRent: Int,
        homeSize: Int,
        numberOfRooms: Int,
        image: MultipartBody.Part?
    ): Single<State> {
        TODO("Not yet implemented")
    }
}