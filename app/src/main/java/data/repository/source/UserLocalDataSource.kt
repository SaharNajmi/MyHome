package data.repository.source

import android.content.SharedPreferences
import common.LOGIN
import common.PHONE
import data.AuthState
import data.repository.LoginUpdate
import io.reactivex.Single
import okhttp3.MultipartBody
import okhttp3.RequestBody

class UserLocalDataSource(val sharedPreferences: SharedPreferences) : UserDataSource {
    override fun login(phone: String, password: String): Single<AuthState> {
        TODO("Not yet implemented")
    }

    override fun signUp(
        phoneNumber: RequestBody,
        username: RequestBody,
        password: RequestBody,
        imageProfile: MultipartBody.Part?
    ): Single<AuthState> {
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
}