package data.repository.source

import data.Banner
import io.reactivex.Completable
import io.reactivex.Single
import services.ApiService

class BannerRemoteDataSource(val apiService: ApiService) : BannerDataSource {

    override fun getBanners(sellOrRent: Int, category: Int): Single<List<Banner>> {
        return apiService.getAllBanner(sellOrRent, category)
    }

    override fun getFavoriteBanners(): Single<List<Banner>> {
        TODO("Not yet implemented")
    }

    override fun addToFavorites(): Completable {
        TODO("Not yet implemented")
    }

    override fun deleteFromFavorites(): Completable {
        TODO("Not yet implemented")
    }
}