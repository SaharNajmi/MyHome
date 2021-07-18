package data.repository

import data.AuthState
import io.reactivex.Single
import okhttp3.MultipartBody
import okhttp3.RequestBody

interface UserRepository {

    fun login(phone: String, password: String): Single<AuthState>

    fun signUp(
        phoneNumber: RequestBody,
        username: RequestBody,
        password: RequestBody,
        imageProfile: MultipartBody.Part?
    ): Single<AuthState>

    fun checkLogin()

    fun signOut()

    fun getPhoneNumber(): String
}