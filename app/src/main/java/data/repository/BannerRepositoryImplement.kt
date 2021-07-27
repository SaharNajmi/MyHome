package data.repository

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

    //موقعی که لیست از سرور گرفته شد باید بررسی کنیم که چه آیتم هایی را در جدول علاقه مندی ها داریم(با گرفتن ایدی آنها)
    //  سپس بررسی میکنیم محصولاتی که از سرور اومدن کدومشون توی ایدی علاقه مندی ها وجود داره
    override fun getBanners(
        sellOrRent: Int,
        category: Int,
        phone: String
    ): Single<List<Banner>> = bannerLocalDataSource.getFavoriteBanners().flatMap { favoriteBanner ->
        bannerRemoteDataSource.getBanners(sellOrRent, category, phone).doOnSuccess {
            //ایدی تمامی علاقه مندی ها را میگیریم
            //map موقعی ک بخایم فقط تعداد خاصی از موجودیت ها را بگیریم: مثلا فقط ای دی ها بگیریم
            val favIds = favoriteBanner.map {
                //آرایه ای از لیست علاقه مندی ها
                it.id
            }
            //برای هر کدوم از آیتم هایی که از سرور اومده چک کنه کنه که ایا این ایدی در لیست عاقه مندی ها است اگه بود یعنی در لیست علاقه مندی ها است
            it.forEach { banner ->
                if (favIds.contains(banner.id))
                    banner.fav = true

            }
        }

    }

    override fun deleteBanner(id: Int): Single<State> = bannerRemoteDataSource.deleteBanner(id)

    override fun getFavoriteBanners(): Single<List<Banner>> =
        bannerLocalDataSource.getFavoriteBanners()

    override fun addToFavorites(banner: Banner): Completable =
        bannerLocalDataSource.addToFavorites(banner)


    override fun deleteFromFavorites(banner: Banner): Completable =
        bannerLocalDataSource.deleteFromFavorites(banner)

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

    override fun addBanner(
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
    ): Single<State> = bannerRemoteDataSource.addBanner(
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
        Timber.i("add banner: " + it.state.toString())
    }
}