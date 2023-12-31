package com.example.myhome.data.repository

import com.example.myhome.data.model.Banner
import com.example.myhome.data.model.State
import io.reactivex.Completable
import io.reactivex.Single
import okhttp3.MultipartBody

interface BannerRepository {

    fun getBanners(
        sellOrRent: Int,
        category: Int,
        phone: String,
        price: String,
        homeSize: Int,
        numberOfRooms: Int
    ): Single<List<Banner>>

    fun deleteBanner(id: Int): Single<State>

    fun getFavoriteBanners(): Single<List<Banner>>

    fun addToFavorites(banner: Banner): Completable

    fun deleteFromFavorites(banner: Banner): Completable

    fun editBanner(
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
    ): Single<State>

    fun addBanner(
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
    ): Single<State>
}