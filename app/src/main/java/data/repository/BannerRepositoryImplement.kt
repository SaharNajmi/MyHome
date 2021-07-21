package data.repository

import data.Banner
import data.State
import data.repository.source.BannerDataSource
import data.repository.source.BannerLocalDataSource
import io.reactivex.Completable
import io.reactivex.Single

class BannerRepositoryImplement(
    val bannerRemoteDataSource: BannerDataSource,
    val bannerLocalDataSource: BannerLocalDataSource
) : BannerRepository {

    override fun getBanners(
        sellOrRent: Int,
        category: Int,
        phone: String
    ): Single<List<Banner>> = bannerRemoteDataSource.getBanners(sellOrRent, category, phone)

    override fun deleteBanner(id: Int): Single<State> = bannerRemoteDataSource.deleteBanner(id)

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