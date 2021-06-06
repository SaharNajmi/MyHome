package com.example.myhome

import android.app.Application
import api.ApiService
import api.createApiServiceInstance
import data.repository.BannerRepository
import data.repository.BannerRepositoryImplement
import data.repository.source.BannerLocalDataSource
import data.repository.source.BannerRemoteDataSource
import main.MainViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin
import org.koin.dsl.module
//این کلاس توی همه پروژه ها انجام میگیره
class App : Application() {
    override fun onCreate() {
        super.onCreate()

        //کوین این دپندونسی را جاهای دیگه اینجکت میکنه برای این کار نیاز است ماژولهای پروژه را تعریف کنیم
        val myModules = module {
            /*single: فقط یکبار اینستنسش ساخته میشه و توی حافظه نگهدای میشه- از دیزاین پترن سینگلتن استفاده میکنه
             factory: هر بار یک شی یا اینستنس جدید ایجاد می کند*/
            single<ApiService> { createApiServiceInstance() }
            factory<BannerRepository> {
                BannerRepositoryImplement(
                    BannerRemoteDataSource(get()),
                    BannerLocalDataSource()
                )
            }
            viewModel { MainViewModel(get()) }
        }
        startKoin {
            androidContext(this@App)
            modules(myModules)
        }
    }
}