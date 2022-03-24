package com.example.myhome.feature.profile

import androidx.lifecycle.MutableLiveData
import com.example.myhome.common.MyHomeSingleObserver
import com.example.myhome.common.MyHomeViewModel
import com.example.myhome.data.Banner
import com.example.myhome.data.State
import com.example.myhome.data.repository.BannerRepository
import com.example.myhome.data.repository.LoginUpdate
import com.example.myhome.data.repository.UserRepository
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import okhttp3.MultipartBody
import okhttp3.RequestBody

class UserViewModel(
    private val userRepository: UserRepository,
    private val bannerRepository: BannerRepository
) : MyHomeViewModel() {

    init {
        getBanner()
    }

    val bannerLiveData = MutableLiveData<List<Banner>>()

    val isSignIn: Boolean
        get() = LoginUpdate.login != false

    val phoneNumber: String
        get() = userRepository.getPhoneNumber()

    fun signOut() = userRepository.signOut()

    fun getUser(phone: String) = userRepository.getUser(phone)

    fun editUser(
        id: RequestBody,
        phoneNumber: RequestBody,
        username: RequestBody,
        password: RequestBody,
        image: MultipartBody.Part?
    ): Single<State> = userRepository.editUser(id, phoneNumber, username, password, image)

    private fun getBanner() {
        bannerRepository.getBanners(0, 0, phoneNumber, "all", 0, 0)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : MyHomeSingleObserver<List<Banner>>(compositeDisposable) {
                override fun onSuccess(t: List<Banner>) {
                    bannerLiveData.value = t
                }
            })
    }

    fun refresh() {
        getBanner()
    }

}