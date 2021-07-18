package feature.profile

import common.MyHomeViewModel
import data.AuthState
import data.repository.LoginUpdate
import data.repository.UserRepository
import io.reactivex.Single
import okhttp3.MultipartBody
import okhttp3.RequestBody

class ProfileViewModel(val userRepository: UserRepository) : MyHomeViewModel() {

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
    ): Single<AuthState> = userRepository.editUser(id, phoneNumber, username, password, image)

}