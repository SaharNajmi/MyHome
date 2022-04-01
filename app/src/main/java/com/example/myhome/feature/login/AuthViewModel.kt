package com.example.myhome.feature.login

import com.example.myhome.common.MyHomeViewModel
import com.example.myhome.data.model.State
import com.example.myhome.data.repository.UserRepository
import io.reactivex.Single
import okhttp3.MultipartBody
import okhttp3.RequestBody

class AuthViewModel(val repository: UserRepository) : MyHomeViewModel() {
    fun login(phone: String, password: String): Single<State> =
        repository.login(phone, password)

    fun signUp(
        phoneNumber: RequestBody,
        username: RequestBody,
        password: RequestBody,
        imageProfile: MultipartBody.Part?
    ): Single<State> = repository.signUp(phoneNumber, username, password, imageProfile)

}