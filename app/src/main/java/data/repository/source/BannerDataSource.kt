package data.repository.source

import data.Banner
import io.reactivex.Completable
import io.reactivex.Single

interface BannerDataSource {

    fun getBanners(): Single<List<Banner>>

    fun getFavoriteBanners(): Single<List<Banner>>

    fun addToFavorites(): Completable

    fun deleteFromFavorites(): Completable
}