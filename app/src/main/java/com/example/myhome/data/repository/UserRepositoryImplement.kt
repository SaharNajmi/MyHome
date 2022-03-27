package com.example.myhome.data.repository

import com.example.myhome.data.State
import com.example.myhome.data.UserInformation
import com.example.myhome.data.repository.source.UserDataSource
import com.example.myhome.data.repository.source.UserLocalDataSource
import io.reactivex.Single
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okio.Buffer

class UserRepositoryImplement(
    private val userLocalDataSource: UserLocalDataSource,
    private val userRemoteDataSource: UserDataSource
) : UserRepository {
    override fun login(phone: String, password: String): Single<State> =
        userRemoteDataSource.login(phone, password).doOnSuccess {
            onSuccessfulLogin(phone, it)
        }

    override fun signUp(
        phoneNumber: RequestBody,
        username: RequestBody,
        password: RequestBody,
        imageProfile: MultipartBody.Part?
    ): Single<State> =
        userRemoteDataSource.signUp(phoneNumber, username, password, imageProfile).flatMap {
            //when user registers -> login and register successful
            userRemoteDataSource.login(
                requestBodyToString(phoneNumber),
                requestBodyToString(password)
            ).doOnSuccess {
                onSuccessfulLogin(requestBodyToString(phoneNumber), it)
            }
        }


    override fun checkLogin() = userLocalDataSource.checkLogin()

    override fun signOut() {
        userLocalDataSource.signOut()
        LoginUpdate.update(false)
    }

    override fun getPhoneNumber(): String = userLocalDataSource.getPhoneNumber()

    override fun getUser(phone: String): Single<UserInformation> =
        userRemoteDataSource.getUser(phone)

    override fun editUser(
        id: RequestBody,
        phoneNumber: RequestBody,
        username: RequestBody,
        password: RequestBody,
        image: MultipartBody.Part?
    ): Single<State> =
        userRemoteDataSource.editUser(id, phoneNumber, username, password, image).flatMap {
            //when user edits profile -> edit profile and login successful
            userRemoteDataSource.login(
                requestBodyToString(phoneNumber),
                requestBodyToString(password)
            ).doOnSuccess {
                onSuccessfulLogin(requestBodyToString(phoneNumber), it)
            }
        }

    private fun onSuccessfulLogin(phone: String, login: State) {
        LoginUpdate.update(login.state)
        userLocalDataSource.saveLogin(login.state)
        userLocalDataSource.savePhoneNumber(phone)
    }

    //convert requestBody To String
    private fun requestBodyToString(requestBody: RequestBody): String {
        val buffer = Buffer()
        requestBody.writeTo(buffer)
        return buffer.readUtf8()

    }
}