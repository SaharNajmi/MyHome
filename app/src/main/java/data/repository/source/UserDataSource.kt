package data.repository.source

import data.AuthState
import data.UserInformation
import io.reactivex.Single
import okhttp3.MultipartBody
import okhttp3.RequestBody

interface UserDataSource {
    fun login(phone: String, password: String): Single<AuthState>

    fun signUp(
        phoneNumber: RequestBody,
        username: RequestBody,
        password: RequestBody,
        imageProfile: MultipartBody.Part?
    ): Single<AuthState>

    fun saveLogin(login: Boolean)

    fun checkLogin()

    fun signOut()

    fun savePhoneNumber(phoneNumber: String)

    fun getPhoneNumber(): String

    fun getUser(phone: String): Single<UserInformation>
}