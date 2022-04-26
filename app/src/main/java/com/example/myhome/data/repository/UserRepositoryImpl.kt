package com.example.myhome.data.repository

import com.example.myhome.data.model.State
import com.example.myhome.data.model.User
import com.example.myhome.data.repository.source.UserDataSource
import io.reactivex.Single
import okhttp3.MultipartBody

class UserRepositoryImpl(
    private val userLocalDataSource: UserDataSource,
    private val userRemoteDataSource: UserDataSource
) : UserRepository {
    override fun login(phone: String, password: String): Single<State> =
        userRemoteDataSource.login(phone, password).doOnSuccess {
            onSuccessfulLogin(phone, it)
        }

    override fun signUp(
        phoneNumber: String,
        username: String,
        password: String,
        imageProfile: MultipartBody.Part?
    ): Single<State> =
        userRemoteDataSource.signUp(phoneNumber, username, password, imageProfile).flatMap {
            //when user registers -> login and register successful
            userRemoteDataSource.login(
                phoneNumber,
                password
            ).doOnSuccess {
                onSuccessfulLogin(phoneNumber, it)
            }
        }


    override fun checkLogin() = userLocalDataSource.checkLogin()

    override fun signOut() {
        userLocalDataSource.signOut()
        LoginUpdate.update(false)
    }

    override fun getPhoneNumber(): String = userLocalDataSource.getPhoneNumber()

    override fun getUser(phone: String): Single<User> =
        userRemoteDataSource.getUser(phone)

    override fun editUser(
        id: String,
        phoneNumber: String,
        username: String,
        password: String,
        image: MultipartBody.Part?
    ): Single<State> =
        userRemoteDataSource.editUser(id, phoneNumber, username, password, image).flatMap {
            //when user edits profile -> edit profile and login successful
            userRemoteDataSource.login(
                phoneNumber,
                password
            ).doOnSuccess {
                onSuccessfulLogin(phoneNumber, it)
            }
        }

    private fun onSuccessfulLogin(phone: String, login: State) {
        LoginUpdate.update(login.state)
        userLocalDataSource.saveLogin(login.state)
        userLocalDataSource.savePhoneNumber(phone)
    }
}