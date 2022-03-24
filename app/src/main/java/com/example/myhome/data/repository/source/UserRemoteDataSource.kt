package com.example.myhome.data.repository.source

import com.example.myhome.data.State
import com.example.myhome.data.UserInformation
import com.example.myhome.services.ApiService
import io.reactivex.Single
import okhttp3.MultipartBody
import okhttp3.RequestBody

class UserRemoteDataSource(private val apiService: ApiService) : UserDataSource {
    override fun login(phone: String, password: String): Single<State> =
        apiService.login(phone, password)

    override fun signUp(
        phoneNumber: RequestBody,
        username: RequestBody,
        password: RequestBody,
        imageProfile: MultipartBody.Part?
    ): Single<State> = apiService.signUp(phoneNumber, username, password, imageProfile)

    override fun saveLogin(login: Boolean) {
        TODO("Not yet implemented")
    }

    override fun checkLogin() {
        TODO("Not yet implemented")
    }

    override fun signOut() {
        TODO("Not yet implemented")
    }

    override fun savePhoneNumber(phoneNumber: String) {
        TODO("Not yet implemented")
    }

    override fun getPhoneNumber(): String {
        TODO("Not yet implemented")
    }

    override fun getUser(phone: String): Single<UserInformation> = apiService.getUser(phone)

    override fun editUser(
        id: RequestBody,
        phoneNumber: RequestBody,
        username: RequestBody,
        password: RequestBody,
        image: MultipartBody.Part?
    ): Single<State> = apiService.editUser(id, phoneNumber, username, password, image)
}