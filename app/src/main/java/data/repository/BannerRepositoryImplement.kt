package data.repository

import data.Banner
import data.repository.source.BannerDataSource
import data.repository.source.BannerLocalDataSource
import io.reactivex.Completable
import io.reactivex.Single

class BannerRepositoryImplement(
    val bannerDataSource: BannerDataSource,
    val bannerLocalDataSource: BannerLocalDataSource
) : BannerRepository {

    override fun getBanners(sellOrRent: Int, category: Int): Single<List<Banner>> =
        bannerDataSource.getBanners(sellOrRent, category)

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