package feature.login

import common.MyHomeViewModel
import data.AuthState
import data.repository.UserRepository
import io.reactivex.Single
import okhttp3.MultipartBody
import okhttp3.RequestBody

class AuthViewModel(val repository: UserRepository) : MyHomeViewModel() {
    fun login(phone: String, password: String): Single<AuthState> =
        repository.login(phone, password)

    fun signUp(
        phoneNumber: RequestBody,
        username: RequestBody,
        password: RequestBody,
        imageProfile: MultipartBody.Part?
    ): Single<AuthState> = repository.signUp(phoneNumber, username, password, imageProfile)

}