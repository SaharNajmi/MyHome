package data.repository

import data.AuthState
import data.UserInformation
import data.repository.source.UserDataSource
import data.repository.source.UserLocalDataSource
import io.reactivex.Single
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okio.Buffer

class UserRepositoryImplement(
    val userLocalDataSource: UserLocalDataSource,
    val userRemoteDataSource: UserDataSource
) : UserRepository {
    override fun login(phone: String, password: String): Single<AuthState> =
        userRemoteDataSource.login(phone, password).doOnSuccess {
            onSuccessfulLogin(phone, it)
        }

    override fun signUp(
        phoneNumber: RequestBody,
        username: RequestBody,
        password: RequestBody,
        imageProfile: MultipartBody.Part?
    ): Single<AuthState> =
        userRemoteDataSource.signUp(phoneNumber, username, password, imageProfile).doFinally {
            //وقتی کاربر ثبتام میکنه هم ثبتنام انجام میشه هم لاگین
            //flatMap: برای اینکه چند تا رکوست با هم انجام بشه
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

    fun onSuccessfulLogin(phone: String, login: AuthState) {
        LoginUpdate.update(login.state)
        userLocalDataSource.saveLogin(login.state)
        userLocalDataSource.savePhoneNumber(phone)
    }

    //convert requestBody To String
    fun requestBodyToString(requestBody: RequestBody): String {
        val buffer = Buffer()
        requestBody.writeTo(buffer)
        return buffer.readUtf8()

    }
}