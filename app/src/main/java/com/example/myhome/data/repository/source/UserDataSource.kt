package com.example.myhome.data.repository.source

import com.example.myhome.data.model.State
import com.example.myhome.data.model.User
import io.reactivex.Single
import okhttp3.MultipartBody

interface UserDataSource {
    fun login(phone: String, password: String): Single<State>

    fun signUp(
        phoneNumber: String,
        username: String,
        password: String,
        imageProfile: MultipartBody.Part?
    ): Single<State>

    fun saveLogin(login: Boolean)

    fun checkLogin()

    fun signOut()

    fun savePhoneNumber(phoneNumber: String)

    fun getPhoneNumber(): String

    fun getUser(phone: String): Single<User>

    fun editUser(
        id: String,
        phoneNumber: String,
        username: String,
        password: String,
        image: MultipartBody.Part?
    ): Single<State>
}