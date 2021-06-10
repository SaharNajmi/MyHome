package data.repository

import data.Banner
import io.reactivex.Completable
import io.reactivex.Single

interface BannerRepository {
    fun getBanners(sellOrRent: Int, category: Int): Single<List<Banner>>

    fun getFavoriteBanners(): Single<List<Banner>>

    fun addToFavorites(): Completable

    fun deleteFromFavorites(): Completable
}