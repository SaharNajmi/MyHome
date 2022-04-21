package com.example.myhome.feature.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.myhome.common.MyHomeSingleObserver
import com.example.myhome.common.MyHomeViewModel
import com.example.myhome.common.asyncNetworkRequest
import com.example.myhome.data.model.Banner
import com.example.myhome.data.model.State
import com.example.myhome.data.model.User
import com.example.myhome.data.repository.BannerRepository
import com.example.myhome.data.repository.LoginUpdate
import com.example.myhome.data.repository.UserRepository
import io.reactivex.Single
import okhttp3.MultipartBody
import okhttp3.RequestBody

class UserViewModel(
    private val userRepository: UserRepository,
    private val bannerRepository: BannerRepository
) : MyHomeViewModel() {

    init {
        getBanner()
        getUser()
    }

    private val _user = MutableLiveData<User>()
    val user: LiveData<User> = _user

    private val _banners = MutableLiveData<List<Banner>>()
    val banners: LiveData<List<Banner>> = _banners

    val isSignIn: Boolean
        get() = LoginUpdate.login != false

    val phoneNumber: String
        get() = userRepository.getPhoneNumber()

    fun signOut() = userRepository.signOut()

    private fun getUser() = userRepository.getUser(userRepository.getPhoneNumber())
        .asyncNetworkRequest()
        .subscribe(object : MyHomeSingleObserver<User>(compositeDisposable) {
            override fun onSuccess(t: User) {
                _user.value = t
            }
        })

    fun editUser(
        id: RequestBody,
        phoneNumber: RequestBody,
        username: RequestBody,
        password: RequestBody,
        image: MultipartBody.Part?
    ): Single<State> = userRepository.editUser(id, phoneNumber, username, password, image)

    fun getBanner() {
        bannerRepository.getBanners(0, 0, phoneNumber, "all", 0, 0)
            .asyncNetworkRequest()
            .subscribe(object : MyHomeSingleObserver<List<Banner>>(compositeDisposable) {
                override fun onSuccess(t: List<Banner>) {
                    _banners.postValue(t)
                }
            })
    }
}