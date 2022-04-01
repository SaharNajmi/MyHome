package com.example.myhome

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.room.Room
import com.example.myhome.data.dp.AppDataBase
import com.example.myhome.data.repository.BannerRepository
import com.example.myhome.data.repository.BannerRepositoryImpl
import com.example.myhome.data.repository.UserRepository
import com.example.myhome.data.repository.UserRepositoryImpl
import com.example.myhome.data.repository.source.*
import com.example.myhome.data.repository.source.local.UserLocalDataSource
import com.example.myhome.data.repository.source.remote.BannerRemoteDataSource
import com.example.myhome.data.repository.source.remote.UserRemoteDataSource
import com.example.myhome.feature.favorite.FavoriteViewModel
import com.example.myhome.feature.home.BannerListAdapter
import com.example.myhome.feature.login.AuthViewModel
import com.example.myhome.feature.main.BannerDetailViewModel
import com.example.myhome.feature.main.BannerViewModel
import com.example.myhome.feature.main.ShareViewModel
import com.example.myhome.feature.profile.UserViewModel
import com.example.myhome.services.ApiService
import com.example.myhome.services.FrescoImageLoadingService
import com.example.myhome.services.ImageLoadingService
import com.example.myhome.services.createApiServiceInstance
import com.facebook.drawee.backends.pipeline.Fresco
import org.koin.android.ext.android.get
import org.koin.android.ext.koin.androidContext
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin
import org.koin.dsl.module
import timber.log.Timber

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
        Fresco.initialize(this)

        val myModules = module {

            single<ApiService> { createApiServiceInstance() }

            single<ImageLoadingService> { FrescoImageLoadingService() }

            single<SharedPreferences> { this@App.getSharedPreferences("app", Context.MODE_PRIVATE) }

            single { Room.databaseBuilder(this@App, AppDataBase::class.java, "db_app").build() }

            factory<BannerRepository> {
                BannerRepositoryImpl(
                    BannerRemoteDataSource(get()),
                    get<AppDataBase>().bannerDao()
                )
            }

            factory { BannerListAdapter(get()) }

            single<UserRepository> {
                UserRepositoryImpl(
                    UserLocalDataSource(get()),
                    UserRemoteDataSource(get())
                )
            }

            viewModel { (cate: Int, sellOrRent: Int, price: String, homeSize: Int, numberOfRooms: Int) ->
                BannerViewModel(get(), cate, sellOrRent, price, homeSize, numberOfRooms)
            }
            viewModel { ShareViewModel() }
            viewModel { (bundle: Bundle) -> BannerDetailViewModel(bundle) }
            viewModel { AuthViewModel(get()) }
            viewModel { UserViewModel(get(), get()) }
            viewModel { FavoriteViewModel(get()) }
        }
        startKoin {
            androidContext(this@App)
            modules(myModules)
        }

        //check login when open application
        val userRepository: UserRepository = get()
        userRepository.checkLogin()

    }
}