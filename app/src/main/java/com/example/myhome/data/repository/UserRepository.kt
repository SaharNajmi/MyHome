package com.example.myhome.data.repository

import com.example.myhome.data.model.State
import com.example.myhome.data.model.User
import io.reactivex.Single
import okhttp3.MultipartBody

interface UserRepository {

    fun login(phone: String, password: String): Single<State>

    fun signUp(
        phoneNumber: String,
        username: String,
        password: String,
        imageProfile: MultipartBody.Part?
    ): Single<State>

    fun checkLogin()

    fun signOut()

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