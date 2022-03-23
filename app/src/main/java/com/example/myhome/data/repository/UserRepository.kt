package com.example.myhome.data.repository

import com.example.myhome.data.State
import com.example.myhome.data.UserInformation
import io.reactivex.Single
import okhttp3.MultipartBody
import okhttp3.RequestBody

interface UserRepository {

    fun login(phone: String, password: String): Single<State>

    fun signUp(
        phoneNumber: RequestBody,
        username: RequestBody,
        password: RequestBody,
        imageProfile: MultipartBody.Part?
    ): Single<State>

    fun checkLogin()

    fun signOut()

    fun getPhoneNumber(): String

    fun getUser(phone: String): Single<UserInformation>

    fun editUser(
        id: RequestBody,
        phoneNumber: RequestBody,
        username: RequestBody,
        password: RequestBody,
        image: MultipartBody.Part?
    ): Single<State>
}