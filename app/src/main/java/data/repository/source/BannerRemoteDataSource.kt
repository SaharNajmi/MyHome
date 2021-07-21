package data.repository.source

import data.Banner
import data.State
import io.reactivex.Completable
import io.reactivex.Single
import services.ApiService

class BannerRemoteDataSource(val apiService: ApiService) : BannerDataSource {
    override fun getBanners(
        sellOrRent: Int,
        category: Int,
        phone: String
    ): Single<List<Banner>> {
        return apiService.getAllBanner(sellOrRent, category, phone)
    }

    override fun deleteBanner(id: Int): Single<State> = apiService.deleteBanner(id)

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