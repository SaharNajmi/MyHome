package data.repository

import data.Banner
import data.State
import io.reactivex.Completable
import io.reactivex.Single

interface BannerRepository {

    fun getBanners(sellOrRent: Int, category: Int, phone: String): Single<List<Banner>>

    fun deleteBanner(id:Int):Single<State>

    fun getFavoriteBanners(): Single<List<Banner>>

    fun addToFavorites(): Completable

    fun deleteFromFavorites(): Completable
}