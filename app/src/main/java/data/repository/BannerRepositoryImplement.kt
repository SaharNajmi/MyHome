package data.repository

import android.drm.ProcessedData
import data.Banner
import data.repository.source.BannerDataSource
import data.repository.source.BannerLocalDataSource
import data.repository.source.BannerRemoteDataSource
import io.reactivex.Completable
import io.reactivex.Single

class BannerRepositoryImplement(
    val bannerDataSource: BannerDataSource,
    val bannerLocalDataSource: BannerLocalDataSource
) : BannerRepository {
    override fun getBanners(): Single<List<Banner>> = bannerDataSource.getBanners()

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