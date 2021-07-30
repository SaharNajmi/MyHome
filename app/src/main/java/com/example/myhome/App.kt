package com.example.myhome

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.room.Room
import com.facebook.drawee.backends.pipeline.Fresco
import data.dp.AppDataBase
import data.repository.BannerRepository
import data.repository.BannerRepositoryImplement
import data.repository.UserRepository
import data.repository.UserRepositoryImplement
import data.repository.source.*
import feature.favorite.FavoriteViewModel
import feature.home.BannerListAdapter
import feature.login.AuthViewModel
import feature.main.BannerDetailViewModel
import feature.main.BannerViewModel
import feature.main.ShareViewModel
import feature.profile.UserViewModel
import org.koin.android.ext.android.get
import org.koin.android.ext.koin.androidContext
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin
import org.koin.dsl.module
import services.ApiService
import services.FrescoImageLoadingService
import services.ImageLoadingService
import services.createApiServiceInstance
import timber.log.Timber

//این کلاس توی همه پروژه ها انجام میگیره
class App : Application() {
    override fun onCreate() {
        super.onCreate()
        //برای استفاده از timber باید آن را داخل کلاس Application فراخوانی کنیم-برای اینکه موقع لاگ گرفتن نام کلاس به عنوان Tag لاگ ذکر شود.
        Timber.plant(Timber.DebugTree())
        //کوین این دپندونسی را جاهای دیگه اینجکت میکنه برای این کار نیاز است ماژولهای پروژه را تعریف کنیم

        //use Fresco for load imageView
        Fresco.initialize(this)

        val myModules = module {

            /*single: فقط یکبار اینستنسش ساخته میشه و توی حافظه نگهدای میشه- از دیزاین پترن سینگلتن استفاده میکنه
             factory: هر بار یک شی یا اینستنس جدید ایجاد می کند*/
            single<ApiService> { createApiServiceInstance() }
            //یعنی اینکه برای لود تصاویر از Fresco استفاده میکنیم
            single<ImageLoadingService> { FrescoImageLoadingService() }

            //اضافه کردن ماژول های مورد نیاز لاگین
            single<SharedPreferences> { this@App.getSharedPreferences("app", Context.MODE_PRIVATE) }

            //add dao room
            single { Room.databaseBuilder(this@App, AppDataBase::class.java, "db_app").build() }

            factory<BannerRepository> {
                BannerRepositoryImplement(
                    BannerRemoteDataSource(get()),
                    get<AppDataBase>().bannerDao()
                )
            }

            factory { BannerListAdapter(get()) }

            single<UserRepository> {
                UserRepositoryImplement(
                    UserLocalDataSource(get()),
                    UserRemoteDataSource(get())
                )
            }

            viewModel { (cate: Int, sellOrRent: Int) -> BannerViewModel(get(), cate, sellOrRent) }
            viewModel {  ShareViewModel() }
            viewModel { (bundle: Bundle) -> BannerDetailViewModel(bundle) }
            viewModel { AuthViewModel(get()) }
            viewModel { UserViewModel(get(), get()) }
            viewModel { FavoriteViewModel(get()) }
        }
        startKoin {
            androidContext(this@App)
            modules(myModules)
        }

        //auto login
        //موقع لود اپلیکیشن، این کلاس فراخوانی میشه و وضعیت لاگین را چک می کند
        //نمونه یا اینستنس ساختن از کلاس ریپازیتوری با استفاده از get کوین
        val userRepository: UserRepository = get()
        userRepository.checkLogin()

    }
}