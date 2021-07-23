package data.repository

import android.widget.Toast
import data.Banner
import data.State
import data.repository.source.BannerDataSource
import data.repository.source.BannerLocalDataSource
import io.reactivex.Completable
import io.reactivex.Single
import okhttp3.MultipartBody
import okhttp3.RequestBody
import timber.log.Timber

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

    override fun editBanner(
        id: Int,
        userID: Int,
        title: RequestBody,
        description: RequestBody,
        price: RequestBody,
        location: RequestBody,
        category: Int,
        sellOrRent: Int,
        homeSize: Int,
        numberOfRooms: Int,
        image: MultipartBody.Part?
    ): Single<State> = bannerRemoteDataSource.editBanner(
        id,
        userID,
        title,
        description,
        price,
        location,
        category,
        sellOrRent,
        homeSize,
        numberOfRooms,
        image
    ).doOnSuccess {
        Timber.i("edit banner: " + it.state.toString())
    }
}