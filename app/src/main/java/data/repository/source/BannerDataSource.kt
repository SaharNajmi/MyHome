package data.repository.source

import data.Banner
import io.reactivex.Completable
import io.reactivex.Single

interface BannerDataSource {

    fun getBanners(sellOrRent: Int, category: Int): Single<List<Banner>>

    fun getFavoriteBanners(): Single<List<Banner>>

    fun addToFavorites(): Completable

    fun deleteFromFavorites(): Completable
}