package feature.profile

import androidx.lifecycle.MutableLiveData
import common.MyHomeSingleObserver
import common.MyHomeViewModel
import data.Banner
import data.State
import data.repository.BannerRepository
import data.repository.LoginUpdate
import data.repository.UserRepository
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import okhttp3.MultipartBody
import okhttp3.RequestBody

class UserViewModel(val userRepository: UserRepository, val bannerRepository: BannerRepository) :
    MyHomeViewModel() {

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

    fun getBanner() {
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