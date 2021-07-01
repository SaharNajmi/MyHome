package com.example.myhome

import feature.adapter.BannerListAdapter
import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import com.facebook.drawee.backends.pipeline.Fresco
import data.repository.BannerRepository
import data.repository.BannerRepositoryImplement
import data.repository.UserRepository
import data.repository.UserRepositoryImplement
import data.repository.source.BannerLocalDataSource
import data.repository.source.BannerRemoteDataSource
import data.repository.source.UserLocalDataSource
import data.repository.source.UserRemoteDataSource
import feature.main.BannerDetailViewModel
import feature.main.MainViewModel
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

            single<SharedPreferences> { this@App.getSharedPreferences("app", Context.MODE_PRIVATE) }

            single<UserRepository> {
                UserRepositoryImplement(
                    UserLocalDataSource(get()),
                    UserRemoteDataSource(get())
                )
            }

            factory<BannerRepository> {
                BannerRepositoryImplement(
                    BannerRemoteDataSource(get()),
                    BannerLocalDataSource()
                )
            }
            factory { BannerListAdapter(get()) }

            viewModel { (cate: Int) -> MainViewModel(get(), cate) }
            viewModel { (bundle: Bundle) -> BannerDetailViewModel(bundle) }
        }
        startKoin {
            androidContext(this@App)
            modules(myModules)
        }
    }
}