package data.repository.source

import data.Banner
import data.State
import io.reactivex.Completable
import io.reactivex.Single

class BannerLocalDataSource : BannerDataSource {
    override fun getBanners(sellOrRent: Int, category: Int, phone: String): Single<List<Banner>> {
        TODO("Not yet implemented")
    }

    override fun deleteBanner(id: Int): Single<State> {
        TODO("Not yet implemented")
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