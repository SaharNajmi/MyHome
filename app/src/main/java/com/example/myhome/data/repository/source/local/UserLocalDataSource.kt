package com.example.myhome.data.repository.source.local

import android.content.SharedPreferences
import com.example.myhome.common.Constants.LOGIN
import com.example.myhome.common.Constants.PHONE
import com.example.myhome.data.model.State
import com.example.myhome.data.model.User
import com.example.myhome.data.repository.LoginUpdate
import com.example.myhome.data.repository.source.UserDataSource
import io.reactivex.Single
import okhttp3.MultipartBody
import okhttp3.RequestBody

class UserLocalDataSource(private val sharedPreferences: SharedPreferences) : UserDataSource {

    override fun login(phone: String, password: String): Single<State> {
        TODO("Not yet implemented")
    }

    override fun signUp(
        phoneNumber: RequestBody,
        username: RequestBody,
        password: RequestBody,
        imageProfile: MultipartBody.Part?
    ): Single<State> {
        TODO("Not yet implemented")
    }

    override fun saveLogin(login: Boolean) {
        sharedPreferences.edit().apply {
            putBoolean(LOGIN, login)
        }.apply()
    }

    override fun checkLogin() {
        LoginUpdate.update(
            sharedPreferences.getBoolean(LOGIN, false)
        )
    }

    override fun signOut() = sharedPreferences.edit().apply() {
        clear()
    }.apply()

    override fun savePhoneNumber(phoneNumber: String) = sharedPreferences.edit().apply {
        putString(PHONE, phoneNumber)
    }.apply()

    override fun getPhoneNumber(): String = sharedPreferences.getString(PHONE, "") ?: ""

    override fun getUser(phone: String): Single<User> {
        TODO("Not yet implemented")
    }

    override fun editUser(
        id: RequestBody,
        phoneNumber: RequestBody,
        username: RequestBody,
        password: RequestBody,
        image: MultipartBody.Part?
    ): Single<State> {
        TODO("Not yet implemented")
    }

}